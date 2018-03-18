package pageobjects;

import driver.DriverManager;
import driver.DriverMethods;
import org.openqa.selenium.By;

public class Google {

    By logo = By.id("logo");
    By luckyButton = By.name("btnI");

    public Google() {

    }

    public void goToGoogle(){
        DriverManager.getDriver().navigate().to("http://google.com");
    }

    public void goToLucky() {
        DriverMethods.clickElement(luckyButton);
    }

    public boolean isLogoDisplayed() {
        return DriverMethods.isDisplayed(logo);
    }
}
