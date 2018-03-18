package steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import pageobjects.Google;

public class MainSteps {

    Google google;

    public MainSteps() {
        google = new Google();
    }

    @Given("^I open Google$")
    public void goToGoogle(){
        google.goToGoogle();
    }

    @When("^I go to the lucky page$")
    public void goToLuckyPage() {
        google.goToLucky();
    }

    @Then("^I see the Google logo$")
    public void logoIsDisplayed(){
        Assert.assertTrue(google.isLogoDisplayed());
    }
}
