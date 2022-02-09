package driver;

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
    static ChromeOptions getDefaultChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_settings.popus", 1);

        options.setExperimentalOption("prefs", prefs);
        options.addArguments("disable-infobars");
        options.addArguments("start-maximized");
        options.addArguments("test-type");

        return options;
    }

    /**
     * Returns default Edge browser options
     * <p>
     *
     * @return EdgeOptions
     */
    static EdgeOptions getDefaultEdgeOptions() {
        EdgeOptions options = new EdgeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_settings.popus", 1);

        options.setExperimentalOption("prefs", prefs);
        options.addArguments("disable-infobars");
        options.addArguments("start-maximized");
        options.addArguments("test-type");

        return options;
    }
}
