package driver;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

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
     * @return ChromeOptions
     */
    static ChromeOptions getDefaultChromeOptions(){
        ChromeOptions options = new ChromeOptions();

        Map<String,Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_settings.popus", 1);

        options.setExperimentalOption("prefs", prefs);
        options.addArguments("disable-infobars");
        options.addArguments("start-maximized");
        options.addArguments("test-type");

        return options;
    }

    /**
     * Returns default Internet Explorer browser options
     * <p>
     * @return InternetExplorerOptions
     */
    static InternetExplorerOptions getDefaultIEOptions(){
        InternetExplorerOptions options = new InternetExplorerOptions();

        options.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
        options.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
        options.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);
        options.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, true);
        options.setCapability(InternetExplorerDriver.NATIVE_EVENTS, true);
        options.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, "");
        options.setCapability(InternetExplorerDriver.NATIVE_EVENTS, true);

        return options;
    }
}
