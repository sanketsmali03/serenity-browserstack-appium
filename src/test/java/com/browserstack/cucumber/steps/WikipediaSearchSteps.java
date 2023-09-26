package com.browserstack.cucumber.steps;

import net.thucydides.core.model.TestOutcome;
import org.openqa.selenium.JavascriptExecutor;

import com.browserstack.cucumber.pages.WikipediaApp;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.Status;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.steps.ScenarioSteps;

public class WikipediaSearchSteps extends ScenarioSteps {
    WikipediaApp wikipediaApp;
    Scenario scenario;

    @Before
    public void before(Scenario scenario) {
        this.scenario = scenario;
    }

    @When("^I type query as \\\"([^\\\"]*)\\\"$")
    public void search_google_for(String searchWord) throws Throwable {
        wikipediaApp.ClickEnterText();
        wikipediaApp.submitForm(searchWord);
    }

    @And("^I submit$")
    public void thenSubmit() throws Throwable {
        Thread.sleep(5000);
    }

    @Then("^I should see results")
    public void matchTitle() throws Throwable {
        wikipediaApp.thereShouldBeElements();
    }

    @After
    public void getSessionDetails() {
        String name = scenario.getName();
        JavascriptExecutor jse = (JavascriptExecutor)wikipediaApp.getDriver();
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionName\", \"arguments\": {\"name\":\""+name+"\" }}");
       String status = scenario.getStatus().toString();
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \""+ status +"\"}}");

    }
}
