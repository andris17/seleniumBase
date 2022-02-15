package driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.function.Function;

import static driver.DriverManager.*;

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
     * @param amountInSeconds The specified wait amount in seconds.
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
        waitForAlertToAppear(getImplicitWaitTimeout());
    }


    /**
     * Waits for an alert to appear.
     *
     * @param timeout The timeout duration in seconds.
     */
    public static void waitForAlertToAppear(long timeout) {
        getDefaultWait(timeout).until(ExpectedConditions.alertIsPresent());
    }

    /**
     * Waits for the specified element to appear.
     *
     * @param locator The specified By locator.
     */
    public static void waitForElementToAppear(By locator) {
        waitForElementToAppear(locator, getImplicitWaitTimeout());
    }

    /**
     * Waits for the specified element to appear.
     *
     * @param locator The specified By locator.
     * @param timeout The timeout duration in seconds.
     */
    public static void waitForElementToAppear(By locator, long timeout) {
        getDefaultWait(timeout).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for the specified element to disappear.
     *
     * @param locator The specified By locator.
     */
    public static void waitForElementToDisappear(By locator) {
        waitForElementToDisappear(locator, getImplicitWaitTimeout());
    }

    /**
     * Waits for the specified element to disappear.
     *
     * @param locator The specified By locator.
     * @param timeout The timeout duration in seconds.
     */
    public static void waitForElementToDisappear(By locator, long timeout) {
        getDefaultWait(timeout).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits for the specified element to contain the value in the attribute.
     *
     * @param locator   The specified By locator.
     * @param attribute The queried attribute's name.
     * @param value     The queried attribute's value.
     */
    public static void waitForAttributeToContain(By locator, String attribute, String value) {
        waitForAttributeToContain(locator, attribute, value, getImplicitWaitTimeout());
    }

    /**
     * Waits for the specified element to contain the value in the attribute.
     *
     * @param locator   The specified By locator.
     * @param attribute The queried attribute's name.
     * @param value     The queried attribute's value.
     * @param timeout   The timeout duration in seconds.
     */
    public static void waitForAttributeToContain(By locator, String attribute, String value, long timeout) {
        getDefaultWait(timeout).until((ExpectedCondition<Boolean>) driver -> {
            assert driver != null;
            return driver.findElement(locator).getAttribute(attribute).contains(value);
        });
    }

    /**
     * Waits for the specified element to not contain the value in the attribute.
     *
     * @param locator   The specified By locator.
     * @param attribute The queried attribute's name.
     * @param value     The queried attribute's value.
     */
    public static void waitForAttributeNotToContain(By locator, String attribute, String value) {
        waitForAttributeNotToContain(locator, attribute, value, getImplicitWaitTimeout());
    }

    /**
     * Waits for the specified element to not contain the value in the attribute.
     *
     * @param locator   The specified By locator.
     * @param attribute The queried attribute's name.
     * @param value     The queried attribute's value.
     * @param timeout   The timeout duration in seconds.
     */
    public static void waitForAttributeNotToContain(By locator, String attribute, String value, long timeout) {
        getDefaultWait(timeout).until((ExpectedCondition<Boolean>) driver -> {
            assert driver != null;
            return !driver.findElement(locator).getAttribute(attribute).contains(value);
        });
    }

    /**
     * Waits for an element to be clickable.
     *
     * @param locator The specified By locator.
     */
    public static void waitForElementToBeClickable(By locator) {
        waitForElementToBeClickable(locator, getImplicitWaitTimeout());
    }

    /**
     * Waits for an element to be clickable.
     *
     * @param locator The specified By locator.
     * @param timeout The timeout duration in seconds.
     */
    public static void waitForElementToBeClickable(By locator, long timeout) {
        getDefaultWait(timeout).until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits for an element to be clickable.
     *
     * @param element The specified WebElement.
     */
    public static void waitForElementToBeClickable(WebElement element) {
        waitForElementToBeClickable(element, getImplicitWaitTimeout());
    }

    /**
     * Waits for an element to be clickable.
     *
     * @param element The specified WebElement.
     * @param timeout The timeout duration in seconds.
     */
    public static void waitForElementToBeClickable(WebElement element, long timeout) {
        getDefaultWait(timeout).until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits for an ExpectedCondition to be true.
     *
     * @param isTrue The specified ExpectedCondition to meet.
     */
    public static <T> void waitForConditionToMeet(ExpectedCondition<T> isTrue){
        waitForConditionToMeet(isTrue, getImplicitWaitTimeout());
    }

    /**
     * Waits for an ExpectedCondition to be true.
     *
     * @param isTrue The specified ExpectedCondition to meet.
     * @param timeout The timeout duration in seconds.
     */
    public static <T> void waitForConditionToMeet(ExpectedCondition<T> isTrue, long timeout){
        getDefaultWait(timeout).until(isTrue);
    }
}
