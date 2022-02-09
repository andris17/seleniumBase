package driver;

import enums.BrowserType;
import enums.ScreenshotMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.openqa.selenium.support.ui.FluentWait;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

/**
 * Responsible for handling the WebDriver during the test run.<br>
 * This class instantiates the driver as a singleton object.<br>
 * Access to the driver can only happen via the getDriver() method.
 *
 * @author Andras Fuge
 */
public class DriverManager {

    private static long DEFAULT_IMPLICIT_WAIT = 2;

    private static WebDriver driver;

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

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(getImplicitWaitSeconds()));
            driver.manage().window().maximize();

            deleteAllCookies();
        }
    }

    private static void initLocalDriver(BrowserType browserType) {
        if (BrowserType.EDGE.equals(browserType)) {
            initLocalDriver(browserType, BrowserOptions.getDefaultEdgeOptions());
        } else {
            initLocalDriver(browserType, BrowserOptions.getDefaultChromeOptions());
        }
    }

    private static void initLocalDriver(BrowserType browserType, MutableCapabilities capabilities) {
        if (driver == null) {
            if (BrowserType.EDGE.equals(browserType)) {
                driver = new ChromeDriver(new ChromeOptions().merge(capabilities));
            } else {
                driver = new EdgeDriver(new EdgeOptions().merge(capabilities));
            }
        }
    }

    private static void initRemoteDriver(BrowserType browserType, String gridHubUrl) throws MalformedURLException {
        if (BrowserType.EDGE.equals(browserType)) {
            initRemoteDriver(gridHubUrl, BrowserOptions.getDefaultChromeOptions());
        } else {
            initRemoteDriver(gridHubUrl, BrowserOptions.getDefaultEdgeOptions());
        }
    }

    private static void initRemoteDriver(String gridHubUrl, MutableCapabilities capabilities) throws MalformedURLException {
        if (driver == null) {
            driver = new RemoteWebDriver(new URL(gridHubUrl), capabilities);
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
            if (BrowserType.EDGE.equals(browserType)) {
                Runtime.getRuntime().exec("taskkill /F /IM chrome.exe /T");
                Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
            } else {
                Runtime.getRuntime().exec("taskkill /F /IM msedgedriver.exe /T");

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
    public static void registerEventHandler(WebDriverListener listener) {
        whenDriverPresent();

        driver = new EventFiringDecorator(listener).decorate(driver);
    }

    /**
     * Un-register specifies event listener to the driver.
     */
    public static void unRegisterEventHandler() {
        driver = new EventFiringDecorator().decorate(driver);
    }

    /**
     * Provides the singleton WebDriver object to the consumers.
     *
     * @return the active WebDriver instance
     */
    public static WebDriver getDriver() {
        return driver;
    }

    /**
     * Provides default framework wait to consumers.
     *
     * @return FluentWait with default parameters
     */
    protected static FluentWait<WebDriver> getDefaultWait() {
        return new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds((DEFAULT_IMPLICIT_WAIT)))
                .pollingEvery(Duration.ofMillis(100))
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

    public static long getImplicitWaitSeconds() {
        return DEFAULT_IMPLICIT_WAIT;
    }

    public static void setImplicitWaitSeconds(int amount) {
        whenDriverPresent();

        DEFAULT_IMPLICIT_WAIT = amount;
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(getImplicitWaitSeconds()));
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
