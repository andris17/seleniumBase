package driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;


/**
 * Responsible for handling event listeners occured during test run.<br>
 * Possible usages: additional logging information, extended waiting mechanism
 *
 * @author Andras Fuge
 */
public class DefaultEventListener implements WebDriverEventListener {
    private static DefaultEventListener instance = null;
    private static Logger logger = null;

    private DefaultEventListener() {
        logger = LogManager.getLogger(this.getClass());
    }

    public static DefaultEventListener getInstance() {
        if (instance == null) {
            instance = new DefaultEventListener();
        }

        return instance;
    }

    @Override
    public void beforeAlertAccept(WebDriver webDriver) {
        logger.debug("Accepting alert...");
    }

    @Override
    public void afterAlertAccept(WebDriver webDriver) {
        logger.debug("Alert accepted.");
    }

    @Override
    public void afterAlertDismiss(WebDriver webDriver) {
        logger.debug("Dismissing alert...");
    }

    @Override
    public void beforeAlertDismiss(WebDriver webDriver) {
        logger.debug("Alert dismissed.");
    }

    @Override
    public void beforeNavigateTo(String s, WebDriver webDriver) {
        logger.debug(String.format("Navigating to: %s", s));
    }

    @Override
    public void afterNavigateTo(String s, WebDriver webDriver) {
        logger.debug(String.format("Navigated to: %s", s));
    }

    @Override
    public void beforeNavigateBack(WebDriver webDriver) {
        logger.debug("Navigating back...");
    }

    @Override
    public void afterNavigateBack(WebDriver webDriver) {
        logger.debug("Navigated back.");
    }

    @Override
    public void beforeNavigateForward(WebDriver webDriver) {
        logger.debug("Navigating forward...");
    }

    @Override
    public void afterNavigateForward(WebDriver webDriver) {
        logger.debug("Navigated forward.");
    }

    @Override
    public void beforeNavigateRefresh(WebDriver webDriver) {
        logger.debug("Refreshing...");

    }

    @Override
    public void afterNavigateRefresh(WebDriver webDriver) {
        logger.debug("Refreshed.");
    }

    @Override
    public void beforeFindBy(By by, WebElement webElement, WebDriver webDriver) {
        logger.debug(String.format("Locating element: %s", by.toString()));
    }

    @Override
    public void afterFindBy(By by, WebElement webElement, WebDriver webDriver) {
        logger.debug(String.format("Located element: %s", by.toString()));
    }

    @Override
    public void beforeClickOn(WebElement webElement, WebDriver webDriver) {
        logger.debug(String.format("Clicking element: %s", webElement.toString()));
    }

    @Override
    public void afterClickOn(WebElement webElement, WebDriver webDriver) {
        logger.debug(String.format("Clicked element: %s", webElement.toString()));
    }

    @Override
    public void beforeChangeValueOf(WebElement webElement, WebDriver webDriver, CharSequence[] charSequences) {
        logger.debug(String.format("Changing element: %s", webElement.toString()));
    }

    @Override
    public void afterChangeValueOf(WebElement webElement, WebDriver webDriver, CharSequence[] charSequences) {
        logger.debug(String.format("Changed element: %s", webElement.toString()));
    }

    @Override
    public void beforeScript(String s, WebDriver webDriver) {
        logger.debug(String.format("Executing script: %s", s));
    }

    @Override
    public void afterScript(String s, WebDriver webDriver) {
        logger.debug(String.format("Executed script: %s", s));
    }

    @Override
    public void onException(Throwable throwable, WebDriver webDriver) {

    }
}
