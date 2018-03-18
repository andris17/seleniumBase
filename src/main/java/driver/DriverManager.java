package driver;

import enums.BrowserType;
import enums.ScreenshotMode;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.openqa.selenium.support.ui.FluentWait;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DriverManager {

    private static int DEFAULT_IMPLICIT_WAIT = 2;

    private static WebDriver driver;
    private static EventFiringWebDriver driverWithEvents;

    private static boolean closeBrowsers = false;
    private static ScreenshotMode screenshotMode = ScreenshotMode.FAILED;

    public static void initDriver(BrowserType browserType) {
        if (closeBrowsers) {
            closeBrowsers(browserType);
        }

        if (driver == null) {

            switch (browserType) {

                case CHROME: {
                    System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
                    driver = new ChromeDriver(BrowserOptions.getDefaultChromeOptions());
                    break;
                }
                case IE: {
                    System.setProperty("webdriver.ie.driver", "drivers/IEDriverServer.exe");
                    driver = new InternetExplorerDriver(BrowserOptions.getDefaultIEOptions());
                    break;
                }
            }

            driver.manage().timeouts().implicitlyWait(getImplicitWaitSeconds(), TimeUnit.SECONDS);
            driver.manage().window().maximize();

            deleteAllCookies();
        }
    }

    private static void isDriverPresent() {
        if (driver == null) {
            throw new IllegalStateException("Driver is not present, it should be initialized first!");
        }
    }

    public static void destroyDriver() {
        if (driver == null) {
            return;
        }

        driver.quit();
        driver = null;
    }

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

    public static void deleteCookie(String cookieName) {
        driver.manage().deleteCookieNamed(cookieName);
    }

    public static void deleteAllCookies() {
        driver.manage().deleteAllCookies();
    }

    public static void registerEventHandler(WebDriverEventListener listener) {
        isDriverPresent();

        driverWithEvents = new EventFiringWebDriver(driver);
        driverWithEvents.register(listener);
    }

    public static void unRegisterEventHandler() {
        if (driverWithEvents != null) {
            driverWithEvents.quit();
        }
    }

    /* Getters, Setters */

    public static WebDriver getDriver() {
        if (driverWithEvents != null) {
            return driverWithEvents;
        }
        return driver;
    }

    protected static FluentWait<WebDriver> getDefaultWait() {
        return new FluentWait<WebDriver>(getDriver())
                .withTimeout(3, TimeUnit.SECONDS)
                .pollingEvery(100, TimeUnit.MILLISECONDS)
                .ignoring(StaleElementReferenceException.class);

    }

    public static WebElement getElement(By locator) throws NoSuchElementException {
        try {
            return getDefaultWait().until((getDriver) -> getDriver.findElement(locator));
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Element is not found with locator: "+locator.toString());
        }
    }

    public static List<WebElement> getElements(By locator) {
        return getDriver().findElements(locator);
    }

    public static int getImplicitWaitSeconds() {
        return DEFAULT_IMPLICIT_WAIT;
    }

    public static void setImplicitWaitSeconds(int amount) {
        isDriverPresent();

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

    /* Browser information */

    public static String getUrl() {
        return getDriver().getCurrentUrl();
    }

    public static String getTitle() {
        return getDriver().getTitle();
    }
}
