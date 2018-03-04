package runners;

import driver.DriverManager;
import enums.BrowserType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        tags = {"@Test"},
        strict = true,
        glue = "stepdefs",
        monochrome = true,
        plugin = {"pretty", "json:jsonoutput.json"},
        features = "src/test/resources/features")

public class DefaultTestRunner {

    @BeforeClass
    public static void beforeClass() {
        System.out.println("Starting test suite...");

        DriverManager.setCloseBrowsers(true);
        DriverManager.initDriver(BrowserType.CHROME);
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("Test suite is finished, browser will be closed.");

        DriverManager.destroyDriver();
    }
}