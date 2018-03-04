package driver;

import enums.BrowserType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class DriverManager {

    private static final int DEFAULT_IMPLICIT_WAIT = 2;

    private static WebDriver driver;


    private static boolean closeBrowsers = false;


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

        }
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void destroyDriver() {
        driver.quit();
        driver = null;
    }

    public static int getImplicitWaitSeconds() {
        return DEFAULT_IMPLICIT_WAIT;
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

    /*
    Getters, Setters
     */

    public static boolean isCloseBrowsers() {
        return closeBrowsers;
    }

    public static void setCloseBrowsers(boolean closeBrowsers) {
        DriverManager.closeBrowsers = closeBrowsers;
    }
}
