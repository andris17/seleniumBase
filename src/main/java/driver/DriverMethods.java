package driver;

import enums.MouseEvent;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
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

/**
 * DriverManager class is responsible for handling the WebDriver during the test run.<br>
 * This class encapsulates the WebDriver API from above layers in the test framework.<br>
 * All interaction with the WebDriver API should happen through methods in this class.
 *
 * @author Andras Fuge
 */
public class DriverMethods {

    private static final Logger LOG = LogManager.getLogger(DriverMethods.class);

    /**
     * Navigates to the specified url.
     *
     * @param url the url to navigate to
     */
    public static void navigate(String url) {
        getDriver().navigate().to(url);
    }

    /**
     * Refreshes the active page.
     */
    public static void refresh() {
        getDriver().navigate().refresh();
    }

    /**
     * Returns the window handles of the browser.
     *
     * @return the set of window handles
     */
    public static Set<String> getTabs() {
        return getDriver().getWindowHandles();
    }

    /**
     * Switches the active window to the specified browser tab based on the title of the tab.
     *
     * @param windowTitle the title of the tab to switch to
     * @throws NoSuchWindowException when the tab is not found
     */
    public static void switchToTab(String windowTitle) throws NoSuchWindowException {
        Set<String> windows = getDriver().getWindowHandles();

        for (String window : windows) {
            getDriver().switchTo().window(window);
            if (getDriver().getTitle().contains(windowTitle)) {
                return;
            }
        }

        String errorMessage = String.format("No window found with title: [%s]", windowTitle);
        LOG.error(errorMessage);
        throw new NoSuchWindowException(errorMessage);
    }

    /**
     * Switches the active window to the specified browser tab based on the desired window handle.
     *
     * @param windowHandler the handler of the tab to switch to
     * @throws NoSuchWindowException when the tab is not found
     */
    public static void switchToWindowHandle(String windowHandler) throws NoSuchWindowException {
        Set<String> windows = getDriver().getWindowHandles();

        for (String window : windows) {
            getDriver().switchTo().window(window);
            if (windowHandler.equals(getDriver().getWindowHandle())) {
                return;
            }
        }

        String errorMessage = String.format("No window found with title: [%s]", windowHandler);
        LOG.error(errorMessage);
        throw new NoSuchWindowException(errorMessage);
    }

    /**
     * Creates and switches to a new browser tab.
     */
    public static void openNewTab() {
        Set<String> oldWindows = getDriver().getWindowHandles();

        executeJavaScript("window.open();");

        Set<String> newWindows = getDriver().getWindowHandles();
        switchToTab(getNewestWindowTitle(oldWindows, newWindows));
    }

    /**
     * Closes the active browser tab.
     */
    public static void closeCurrentTab() {
        getDriver().close();
    }

    /**
     * Provides the last opened browser tab based on the difference of two sets of tabs.
     *
     * @param oldWindows first list of browser tabs
     * @param newWindows first list of browser tabs
     * @return first tab not in the intersection of the two sets
     */
    public static String getNewestWindowTitle(Set<String> oldWindows, Set<String> newWindows) {
        for (String title : newWindows) {
            if (!oldWindows.contains(title)) {
                return title;
            }
        }

        return null;
    }

    /**
     * Switches the WebDriver context to the specified frame.
     *
     * @param locator By locator of the frame to switch to
     */
    public static void setFrame(By locator) {
        getDriver().switchTo().defaultContent().switchTo().frame(getElement(locator));
    }

    /**
     * Clicks on the specified element.
     *
     * @param locator By locator of the element to click on
     */
    public static void clickElement(By locator) {
        DriverManager.getElement(locator).click();
    }

    /**
     * Executes the specified mouse action.
     *
     * @param locator By locator of the element to execute the action on.
     * @param event   the specified mouse action of the type MouseEvent
     * @see MouseEvent
     */
    public static void mouseAction(By locator, MouseEvent event) {
        PointerInput mouse = new PointerInput(PointerInput.Kind.MOUSE, "Mouse");
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
                actions.release();
                break;
            case MOUSE_DOWN:
                actions.clickAndHold(element);
                break;
            case MOUSE_HOVER:
                actions.moveToElement(element);
                break;
            case MOUSE_TO_ORIGO:
                actions.moveByOffset(-element.getLocation().getX(), -element.getLocation().getY());
                break;
            default:
        }
    }

    /**
     * Sends a specified key to the element.
     *
     * @param locator By locator of the element
     * @param key     the specified key of type Keys
     */
    public static void sendKey(By locator, Keys key) {
        getElement(locator).sendKeys(key);
    }

    /**
     * Clears and sets the text of an element.
     *
     * @param locator By locator of the element
     * @param input   the specified text input
     */
    public static void setText(By locator, String input) {
        clear(locator);
        getElement(locator).sendKeys(input);
    }

    /**
     * Clears the content of an element.
     *
     * @param locator By locator of the element
     */
    public static void clear(By locator) {
        getElement(locator).clear();
    }

    /**
     * Sets the status of a checkbox.
     *
     * @param locator By locator of the element
     * @param status  status to be set
     */
    public static void setCheckboxStatus(By locator, boolean status) {
        WebElement checkbox = getElement(locator);
        if (status != checkbox.isSelected()) {
            checkbox.click();
        }
    }

    /**
     * Selects an item from the dropdown options by its text.
     *
     * @param locator  By locator of the dropdown
     * @param itemText text of the item to be selected
     */
    public static void selectItemFromDropdown(By locator, String itemText) {
        try {
            Select select = new Select(getElement(locator));
            select.selectByVisibleText(itemText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Selects an item from the dropdown options by its order number.
     *
     * @param locator   By locator of the dropdown
     * @param itemOrder order number of the item to be selected
     */
    public static void selectItemFromDropdown(By locator, int itemOrder) {
        try {
            Select select = new Select(getElement(locator));
            select.selectByIndex(itemOrder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Selects an item from the dropdown options by its value.
     *
     * @param locator By locator of the dropdown
     * @param value   value of the item to be selected
     */
    public static void selectValueFromDropdown(By locator, String value) {
        try {
            Select select = new Select(getElement(locator));
            select.selectByVisibleText(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the selected option's text of the specified dropdown.
     *
     * @param locator By locator of the dropdown
     * @return text of the selected option
     */
    public static String getSelectedOption(By locator) {
        Select select = new Select(getElement(locator));

        return select.getFirstSelectedOption().getText();
    }

    /**
     * Returns all options of the specified dropdown
     *
     * @param locator By locator of the dropdown
     * @return the list of option texts
     */
    public static List<String> getOptions(By locator) {
        Select select = new Select(getElement(locator));
        List<WebElement> elements = select.getOptions();
        List<String> options = new ArrayList<>();

        for (WebElement element : elements) {
            options.add(element.getText());
        }

        return options;
    }

    /**
     * Returns the specified attribute of an element.
     *
     * @param locator   By locator of the element
     * @param attribute the attribute to be returned
     * @return value of the element's attribute
     */
    public static String getAttribute(By locator, String attribute) {
        return getElement(locator).getAttribute(attribute);
    }

    /**
     * Returns the text of an element.
     *
     * @param locator By locator of the element
     * @return text of the element
     */
    public static String getText(By locator) {
        return getElement(locator).getText();
    }

    /* Element status */

    /**
     * Returns the display status of an element.
     *
     * @param locator By locator of the element
     * @return boolean value of the display status
     */
    public static boolean isDisplayed(By locator) {
        return getElement(locator).isDisplayed();
    }

    /**
     * Returns the enabled status of an element.
     *
     * @param locator By locator of the element
     * @return boolean value of the enabled status
     */
    public static boolean isEnabled(By locator) {
        return getElement(locator).isEnabled();
    }

    /**
     * Returns the presence status of an element.
     *
     * @param locator By locator of the element
     * @return boolean value of the presence status
     */
    public static boolean isPresent(By locator) {
        try {
            getElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Accepts a standard browser alert.
     */
    public static void acceptAlert() {
        getDriver().switchTo().alert().accept();
        getDriver().switchTo().defaultContent();
    }

    /**
     * Dismisses a standard browser alert
     */
    public static void dismissAlert() {
        getDriver().switchTo().alert().dismiss();
        getDriver().switchTo().defaultContent();
    }

    /**
     * Captures screenshot of the page. The default capture area is the entire page.
     *
     * @return the screenshot image as a byte array
     * @throws IOException when the output image cannot be created
     */
    public static byte[] takeScreenShot() throws IOException {
        ByteArrayOutputStream screenshot = new ByteArrayOutputStream();
        ImageIO.write(new AShot().shootingStrategy(ShootingStrategies.viewportNonRetina(100, 0, 0)).takeScreenshot(getDriver()).getImage(), "png", screenshot);

        screenshot.flush();

        return screenshot.toByteArray();
    }

    /**
     * Captures screenshot of a specified element.
     *
     * @param locator By locator of the element
     * @return the screenshot as a byte array
     */
    public static byte[] takeScreenShotOfElement(By locator) {
        return getElement(locator).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Attaches the screenshot to a Cucumber scenario.
     *
     * @param scenario   the specified scenario to get extended with screenshot
     * @param screenshot the image to attach
     */
    public static void addScreenshotToScenario(Scenario scenario, byte[] screenshot) {
        writeScreenshotToScenario(scenario, screenshot);
    }

    private static void writeScreenshotToScenario(Scenario scenario, byte[] screenshot) {
        scenario.attach(screenshot, "image/png", "screenshot");
    }

    /**
     * Executes a specified Javascript script in the browser.
     *
     * @param script script to be executed
     * @param args   script arguments
     * @return return value of the Javascript command
     */
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
