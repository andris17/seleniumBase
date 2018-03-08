package driver;

import org.openqa.selenium.*;

import java.util.Set;

import static driver.DriverManager.getDriver;

public class DriverMethods {
    public static void navigate(String url) {
        getDriver().navigate().to(url);
    }

    public static void refresh() {
        getDriver().navigate().refresh();
    }

    public static void switchToTab(String windowTitle) throws NoSuchWindowException {
        Set<String> windows = getDriver().getWindowHandles();

        for (String window : windows) {
            getDriver().switchTo().window(window);
            if (getDriver().getTitle().contains(windowTitle)) {
                return;
            }
        }

        throw new NoSuchWindowException(String.format("No window found with title: [%s]", windowTitle));
    }

    public static void openNewWindow() {
        Set<String> oldWindows = getDriver().getWindowHandles();

        executeJavaScript("window.open();");

        Set<String> newWindows = getDriver().getWindowHandles();
        switchToTab(getNewestWindowTitle(oldWindows, newWindows));
    }

    public static String getNewestWindowTitle(Set<String> oldWindows, Set<String> newWindows) {
        for (String title : newWindows) {
            if (!newWindows.contains(title)) {
                return title;
            }
        }

        return null;
    }

    public static Object executeJavaScript(String script, Object... args) {
        JavascriptExecutor executor;
        if (getDriver() instanceof JavascriptExecutor) {
            executor = (JavascriptExecutor) getDriver();

            return args.length > 0 ? executor.executeScript(script, args) : executor.executeScript(script);
        } else {
            throw new SessionNotCreatedException("WebDriver is not found!");
        }
    }
}
