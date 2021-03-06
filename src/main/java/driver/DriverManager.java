package driver;

import enums.BrowserType;
import enums.ScreenshotMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.openqa.selenium.support.ui.FluentWait;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Responsible for handling the WebDriver during the test run.<br>
 * This class instantiates the driver as a singleton object.<br>
 * Access to the driver can only happen via the getDriver() method.
 *
 * @author Andras Fuge
 */
public class DriverManager {

    private static int DEFAULT_IMPLICIT_WAIT = 2;

    private static WebDriver driver;
    private static EventFiringWebDriver driverWithEvents;

    private static boolean closeBrowsers = false;
    private static ScreenshotMode screenshotMode = ScreenshotMode.FAILED;
    private static Logger logger = LogManager.getLogger(DriverManager.class);

    /**
     * Instantiates singleton WebDriver for the specified browser type.
     *
     * @param browserType the browser type to be initialized
     * @param gridHubUrl  the address of the selenium grid hub
     */
    public static void initDriver(BrowserType browserType, String... gridHubUrl) {
        System.setProperty("webdriver.ie.driver", "drivers/IEDriverServer.exe");
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");

        if (closeBrowsers) {
            closeBrowsers(browserType);
        }

        if (driver == null) {
            if (gridHubUrl.length > 0) {
                try {
                    initRemoteDriver(browserType, gridHubUrl[0]);
                } catch (MalformedURLException e) {
                    logger.error(String.format("Grid Url is not properly formatted: %s", gridHubUrl[0]));
                    System.exit(1);
                }
            } else {
                initLocalDriver(browserType);
            }

            driver.manage().timeouts().implicitlyWait(getImplicitWaitSeconds(), TimeUnit.SECONDS);
            driver.manage().window().maximize();

            deleteAllCookies();
        }
    }

    private static void initLocalDriver(BrowserType browserType) {
        switch (browserType) {
            case CHROME: {
                initLocalDriver(browserType, BrowserOptions.getDefaultChromeOptions());
                break;
            }
            case IE: {
                initLocalDriver(browserType, BrowserOptions.getDefaultIEOptions());
                break;
            }
        }
    }

    private static void initLocalDriver(BrowserType browserType, MutableCapabilities capabilities) {
        if (driver == null) {
            switch (browserType) {
                case CHROME: {
                    driver = new ChromeDriver(capabilities);
                    break;
                }
                case IE: {
                    driver = new InternetExplorerDriver(capabilities);
                    break;
                }
            }
        }
    }

    private static void initRemoteDriver(BrowserType browserType, String gridHubUrl) throws MalformedURLException {
        switch (browserType) {
            case CHROME: {
                initRemoteDriver(browserType, gridHubUrl, BrowserOptions.getDefaultChromeOptions());
                break;
            }
            case IE: {
                initRemoteDriver(browserType, gridHubUrl, BrowserOptions.getDefaultIEOptions());
                break;
            }
        }
    }

    private static void initRemoteDriver(BrowserType browserType, String gridHubUrl, MutableCapabilities capabilities) throws MalformedURLException {
        if (driver == null) {
            switch (browserType) {
                case CHROME: {
                    driver = new RemoteWebDriver(new URL(gridHubUrl), capabilities);
                    break;
                }
                case IE: {
                    driver = new RemoteWebDriver(new URL(gridHubUrl), capabilities);
                    break;
                }
            }
        }
    }

    /**
     * Stops execution with an exception if called and driver is not yet initialized.
     *
     * @throws IllegalStateException thrown when driver is not initialized
     */
    private static void whenDriverPresent() throws IllegalStateException {
        if (driver == null) {
            logger.error("Driver is not present, IllegalStateException is thrown!");
            throw new IllegalStateException("Driver is not present, it should be initialized first!");
        }
    }

    /**
     * Clean-up method for quitting the running WebDriver instance.
     */
    public static void destroyDriver() {
        if (driver == null) {
            return;
        }

        driver.quit();
        driver = null;
    }

    /**
     * Closes all running browser instances of the type of specified browser.
     *
     * @param browserType the browser type to be closed
     */
    public static void closeBrowsers(BrowserType browserType) {
        try {
            switch (browserType) {
                case CHROME: {
                    Runtime.getRuntime().exec("taskkill /F /IM chrome.exe /T");
                    Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
                    break;
                }
                case IE: {
                    Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe /T");
                    break;
                }
                default:
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes one specified cookie element from client side.
     *
     * @param cookieName the name of the cookie to be deleted
     */
    public static void deleteCookie(String cookieName) {
        driver.manage().deleteCookieNamed(cookieName);
    }

    /**
     * Deletes all client-side cookies.
     */
    public static void deleteAllCookies() {
        driver.manage().deleteAllCookies();
    }

    /**
     * Register specifies event listener to the driver.
     *
     * @param listener an instance of the listener class
     */
    public static void registerEventHandler(WebDriverEventListener listener) {
        whenDriverPresent();

        driverWithEvents = new EventFiringWebDriver(driver);
        driverWithEvents.register(listener);
    }

    /**
     * Un-register specifies event listener to the driver.
     */
    public static void unRegisterEventHandler() {
        if (driverWithEvents != null) {
            driverWithEvents.quit();
        }
    }

    /**
     * Provides the singleton WebDriver object to the consumers.
     *
     * @return the active WebDriver instance
     */
    public static WebDriver getDriver() {
        if (driverWithEvents != null) {
            return driverWithEvents;
        }
        return driver;
    }

    /**
     * Provides default framework wait to consumers.
     *
     * @return FluentWait with default parameters
     */
    protected static FluentWait<WebDriver> getDefaultWait() {
        return new FluentWait<>(getDriver())
                .withTimeout(DEFAULT_IMPLICIT_WAIT, TimeUnit.SECONDS)
                .pollingEvery(100, TimeUnit.MILLISECONDS)
                .ignoring(StaleElementReferenceException.class);
    }

    /**
     * Returns a particular WebElement for the specified locator.
     *
     * @param locator the By locator of the element
     * @return the located WebElement
     * @throws NoSuchElementException when the element is not found
     */
    public static WebElement getElement(By locator) throws NoSuchElementException {
        return getDefaultWait().until((getDriver) -> getDriver.findElement(locator));
    }

    /**
     * Returns all matching WebElements for the specified locator.
     *
     * @param locator the By locator of the element
     * @return the located WebElements
     */
    public static List<WebElement> getElements(By locator) {
        return getDriver().findElements(locator);
    }

    public static int getImplicitWaitSeconds() {
        return DEFAULT_IMPLICIT_WAIT;
    }

    public static void setImplicitWaitSeconds(int amount) {
        whenDriverPresent();

        DEFAULT_IMPLICIT_WAIT = amount;
        driver.manage().timeouts().implicitlyWait(getImplicitWaitSeconds(), TimeUnit.SECONDS);
    }

    public static void setScreenshotMode(ScreenshotMode mode) {
        screenshotMode = mode;
    }

    public static ScreenshotMode getScreenshotMode() {
        return screenshotMode;
    }

    public static boolean isCloseBrowsers() {
        return closeBrowsers;
    }

    public static void setCloseBrowsers(boolean status) {
        closeBrowsers = status;
    }

    public static String getUrl() {
        return getDriver().getCurrentUrl();
    }

    public static String getTitle() {
        return getDriver().getTitle();
    }
}
