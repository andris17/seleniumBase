package driver;

import enums.BrowserType;
import enums.ScreenshotMode;
import org.openqa.selenium.MutableCapabilities;

public class BrowserParameters {

    private BrowserType browserType = BrowserType.CHROME;
    private MutableCapabilities capabilities = null;
    private boolean closeBrowsersAtStart = true;
    private String gridHubUrl = null;
    private ScreenshotMode screenShotMode = ScreenshotMode.FAILED;

    public BrowserParameters() {
    }

    public BrowserParameters closeBrowsersAtStart() {
        this.closeBrowsersAtStart = true;
        return this;
    }

    public BrowserParameters doNotCloseBrowsersAtStart() {
        this.closeBrowsersAtStart = false;
        return this;
    }

    public BrowserType getBrowserType() {
        return browserType;
    }

    public MutableCapabilities getCapabilities() {
        return capabilities;
    }

    public String getGridHubUrl() {
        return gridHubUrl;
    }

    public ScreenshotMode getScreenShotMode() {
        return screenShotMode;
    }

    public boolean isCloseBrowsersAtStart() {
        return closeBrowsersAtStart;
    }

    public BrowserParameters setBrowserType(BrowserType browserType) {
        this.browserType = browserType;
        return this;
    }

    public BrowserParameters setCapabilities(MutableCapabilities capabilities) {
        this.capabilities = capabilities;
        return this;
    }

    public BrowserParameters setGridHubUrl(String gridHubUrl) {
        this.gridHubUrl = gridHubUrl;
        return this;
    }

    public BrowserParameters setScreenShotMode(ScreenshotMode screenShotMode) {
        this.screenShotMode = screenShotMode;
        return this;
    }
}
