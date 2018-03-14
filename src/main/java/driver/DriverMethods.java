package driver;

import cucumber.api.Scenario;
import enums.MouseEvent;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.internal.Locatable;
import org.openqa.selenium.support.ui.Select;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static driver.DriverManager.getDriver;
import static driver.DriverManager.getElement;

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

    public static void openNewTab() {
        Set<String> oldWindows = getDriver().getWindowHandles();

        executeJavaScript("window.open();");

        Set<String> newWindows = getDriver().getWindowHandles();
        switchToTab(getNewestWindowTitle(oldWindows, newWindows));
    }

    public static void closeCurrentTab() {
        getDriver().close();
    }

    public static String getNewestWindowTitle(Set<String> oldWindows, Set<String> newWindows) {
        for (String title : newWindows) {
            if (!newWindows.contains(title)) {
                return title;
            }
        }

        return null;
    }

    public static void setFrame(By locator) {
        getDriver().switchTo().defaultContent().switchTo().frame(getElement(locator));
    }

    public static void clickElement(By locator) {
        DriverManager.getElement(locator).click();
    }

    public static void mouseAction(By locator, MouseEvent event) {
        Mouse mouse = ((HasInputDevices) getDriver()).getMouse();
        Actions actions = new Actions(getDriver());
        WebElement element = DriverManager.getElement(locator);

        switch (event) {
            case CLICK:
                clickElement(locator);
                break;
            case DOUBLE_CLICK:
                actions.doubleClick(element).build().perform();
                break;
            case RIGHT_CLICK:
                actions.contextClick(element).build().perform();
                break;
            case MOUSE_UP:
                mouse.mouseUp(((Locatable) (element)).getCoordinates());
                break;
            case MOUSE_DOWN:
                mouse.mouseDown(((Locatable) (element)).getCoordinates());
                break;
            case MOUSE_HOVER:
                mouse.mouseMove(((Locatable) (element)).getCoordinates());
                break;
            case MOUSE_TO_ORIGO:
                actions.moveByOffset(-element.getLocation().getX(), -element.getLocation().getY());
                break;
            default:
        }
    }

    public static void sendKey(By locator, Keys key) {
        getElement(locator).sendKeys(key);
    }

    public static void setText(By locator, String input) {
        getElement(locator).sendKeys(input);
    }

    public static void selectItemFromDropdown(By locator, String itemText) {
        try {
            Select select = new Select(getElement(locator));
            select.selectByVisibleText(itemText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void selectItemFromDropdown(By locator, int itemOrder) {
        try {
            Select select = new Select(getElement(locator));
            select.selectByIndex(itemOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void selectValueFromDropdown(By locator, String value) {
        try {
            Select select = new Select(getElement(locator));
            select.selectByVisibleText(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSelectedOption(By locator) {
        Select select = new Select(getElement(locator));

        return select.getFirstSelectedOption().getText();
    }

    public static List<String> getOptions(By locator) {
        Select select = new Select(getElement(locator));
        List<WebElement> elements = select.getOptions();
        List<String> options = new ArrayList<>();

        for (WebElement element : elements) {
            options.add(element.getText());
        }

        return options;
    }

    public static String getAttribute(By locator, String attribute) {
        return getElement(locator).getAttribute(attribute);
    }

    public static String getText(By locator) {
        return getElement(locator).getText();
    }

    /* Element status */

    public static boolean isDisplayed(By locator) {
        return getElement(locator).isDisplayed();
    }

    public static boolean isEnabled(By locator) {
        return getElement(locator).isEnabled();
    }

    public static boolean isPresent(By locator) {
        try {
            getElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static void acceptAlert() {
        getDriver().switchTo().alert().accept();
        getDriver().switchTo().defaultContent();
    }

    public static void dismissAlert() {
        getDriver().switchTo().alert().dismiss();
        getDriver().switchTo().defaultContent();
    }

    public static byte[] takeScreenShot() throws IOException {
        ByteArrayOutputStream screenshot = new ByteArrayOutputStream();
        ImageIO.write(new AShot().shootingStrategy(ShootingStrategies.viewportRetina(100, 0, 0, 2)).takeScreenshot(getDriver()).getImage(), "png", screenshot);

        screenshot.flush();

        return screenshot.toByteArray();
    }

    public static byte[] takeScreenShotOfElement(By locator) throws IOException {
        ByteArrayOutputStream screenshot = new ByteArrayOutputStream();
        ImageIO.write(new AShot().takeScreenshot(getDriver(), getElement(locator)).getImage(), "png", screenshot);

        screenshot.flush();

        return screenshot.toByteArray();
    }

    public static void addScreenshotToScenario(Scenario scenario, byte[] screenshot) {
        switch (DriverManager.getScreenshotMode()) {
            case FAILED:
                if (scenario.isFailed()) {
                    writeScreenshotToScenario(scenario, screenshot);
                }
                break;
            case PASSED:
                if (!scenario.isFailed()) {
                    writeScreenshotToScenario(scenario, screenshot);
                }
                break;
            case ALL:
                break;
            default:
                writeScreenshotToScenario(scenario, screenshot);
        }
    }

    public static void writeScreenshotToScenario(Scenario scenario, byte[] screenshot) {
        scenario.embed(screenshot, "image/png");
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
