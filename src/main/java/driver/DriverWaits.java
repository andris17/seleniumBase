package driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static driver.DriverManager.getDefaultWait;
import static driver.DriverManager.getDriver;

/**
 * Class to manage different kind of waits.
 *
 * @author andris17
 */
public final class DriverWaits {

    private DriverWaits() {
    }

    /**
     * Waits for a specified amount of time.
     * 
     * @param amountInSeconds
     */
    public static void waitForSeconds(Integer amountInSeconds) {
        try {
            synchronized (getDriver()) {
                getDriver().wait(amountInSeconds * 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Waits for an alert to appear.
     */
    public static void waitForAlertToAppear() {
        getDefaultWait().until(ExpectedConditions.alertIsPresent());
    }

    /**
     * Waits for the specified element to appear.
     * 
     * @param locator
     */
    public static void waitForElementToAppear(By locator) {
        getDefaultWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for the specified element to disappear.
     *
     * @param locator
     */
    public static void waitForElementToDisappear(By locator) {
        getDefaultWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits for the specified element to contain the value in the attribute.
     *
     * @param locator
     * @param attribute
     * @param value
     */
    public static void waitForAttributeToContain(By locator, String attribute, String value) {
        getDefaultWait().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return driver.findElement(locator).getAttribute(attribute).contains(value);
            }
        });
    }

    /**
     * Waits for the specified element to not contain the value in the attribute.
     *
     * @param locator
     * @param attribute
     * @param value
     */
    public static void waitForAttributeNotToContain(By locator, String attribute, String value) {
        getDefaultWait().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return !driver.findElement(locator).getAttribute(attribute).contains(value);
            }
        });
    }

    /**
     * Waits for an element to be clickable.
     *
     * @param locator
     */
    public static void waitForElementToBeClickable(By locator) {
        getDefaultWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits for an element to be clickable.
     *
     * @param element
     */
    public static void waitForElementToBeClickable(WebElement element) {
        getDefaultWait().until(ExpectedConditions.elementToBeClickable(element));
    }

}
