package driver;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for providing the default browser settings.
 *
 * @author Andras Fuge
 */
class BrowserOptions {

    /**
     * Returns default Chrome browser options
     * <p>
     *
     * @return ChromeOptions
     */
    public static ChromeOptions getDefaultChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        options.setCapability("enableVNC", true);
        options.setCapability("enableVideo", true);

        return options;
    }

    /**
     * Returns default Edge browser options
     * <p>
     *
     * @return EdgeOptions
     */
    public static EdgeOptions getDefaultEdgeOptions() {
        EdgeOptions options = new EdgeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_settings.popus", 1);

        options.setExperimentalOption("prefs", prefs);
        options.addArguments("disable-infobars");
        options.addArguments("start-maximized");
        options.addArguments("test-type");

        return options;
    }

    /**
     * Returns default Selenoid capabilities
     * <p>
     *
     * @return MutableCapabilities
     */
    public static MutableCapabilities getSelenoidCapabilities() {
        MutableCapabilities capabilities = new MutableCapabilities();

        capabilities.setCapability("enableLog", true);
        capabilities.setCapability("enableVideo", true);
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("sessionTimeout", "30m");

        return capabilities;
    }
}
