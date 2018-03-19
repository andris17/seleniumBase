package runners;

import driver.DefaultEventListener;
import driver.DriverManager;
import enums.BrowserType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import utils.Log4jUtils;

@RunWith(Cucumber.class)
@CucumberOptions(
        tags = {"@Test"},
        strict = true,
        glue = {"steps"},
        monochrome = true,
        plugin = {"pretty", "json:jsonoutput.json"},
        features = "src/test/resources/features")

public class DefaultTestRunner {

    @BeforeClass
    public static void beforeClass() {
        System.out.println("Starting test suite...");
        System.setProperty("log4j.configurationFile", "src\\main\\resources\\log4j2.properties.xml");

        DriverManager.setCloseBrowsers(false);

        DriverManager.initDriver(BrowserType.CHROME);

        if (System.getProperty("logLevel") != null) {
            Log4jUtils.setRootLevel(System.getProperty("logLevel"));
        }

        if (System.getProperty("classLogLevel") != null && System.getProperty("classLogName") != null) {
            Log4jUtils.setLevel(System.getProperty("classLogName"), System.getProperty("classLogLevel"));
        }

        DriverManager.registerEventHandler(DefaultEventListener.getInstance());
        DriverManager.setImplicitWaitSeconds(2);
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("Test suite is finished, browser will be closed.");

        DriverManager.destroyDriver();
    }
}