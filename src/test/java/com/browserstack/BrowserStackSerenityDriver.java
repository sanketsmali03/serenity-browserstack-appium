package com.browserstack;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.cucumber.java.After;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.util.SystemEnvironmentVariables;
import net.thucydides.core.webdriver.DriverSource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.AppiumDriver;
import net.thucydides.core.model.TestOutcome;

import java.net.URL;
import java.util.Iterator;

public class BrowserStackSerenityDriver implements DriverSource {

    private AppiumDriver driver; // Declare a class-level driver variable


    public AppiumDriver newDriver() {
        EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

        String username = System.getenv("BROWSERSTACK_USERNAME");
        if (username == null) {
            username = (String) environmentVariables.getProperty("browserstack.user");
        }

        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
        if (accessKey == null) {
            accessKey = (String) environmentVariables.getProperty("browserstack.key");
        }

        String environment = System.getProperty("environment");
        DesiredCapabilities capabilities = new DesiredCapabilities();

        Iterator it = environmentVariables.getKeys().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();

            if (key.equals("browserstack.user") || key.equals("browserstack.key")
                    || key.equals("browserstack.server")) {
                continue;
            } else if (key.startsWith("bstack_")) {
                if (key.contains("app")) {
                    String appId = System.getenv("BROWSERSTACK_APP_ID");
                    if (appId == null) {
                        appId = environmentVariables.getProperty(key);
                    }
                    capabilities.setCapability(key.replace("bstack_", ""), appId);
                } else
                    capabilities.setCapability(key.replace("bstack_", ""), environmentVariables.getProperty(key));
                if (key.equals("bstack_browserstack.local")
                        && environmentVariables.getProperty(key).equalsIgnoreCase("true")) {
                    System.setProperty("browserstack.local", "true");
                }
            } else if (environment != null && key.startsWith("environment." + environment)) {
                capabilities.setCapability(key.replace("environment." + environment + ".", ""),
                        environmentVariables.getProperty(key));
                if (key.equals("environment." + environment + ".browserstack.local")
                        && environmentVariables.getProperty(key).equalsIgnoreCase("true")) {
                    System.setProperty("browserstack.local", "true");
                }
            }
        }
        System.out.println(capabilities.asMap());
        try {
            if (capabilities.getCapability("device").toString().contains("iP")) {
                driver = new IOSDriver<IOSElement>(new URL("https://" + username + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub"), capabilities);
                System.out.println(driver);
                return driver;
            } else {
                driver = new AndroidDriver<AndroidElement>(new URL("https://" + username + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub"), capabilities);
                System.out.println(driver);
                return driver;
            }
        } catch (Exception e) {
            System.out.println("@@@\t" + e.getMessage() + "\n!!!\t" + e.getCause());
//            System.out.println(e);
            return null;
        }
    }

    public AppiumDriver getDriver() {
        return driver;
    }


   /* @After // Add the @After annotation
    public void afterScenario(TestOutcome testOutcome) {
        // Inside the @After method, execute JavaScript using JavascriptExecutor
        System.out.println("Test Outcode - " + testOutcome);
        JavascriptExecutor jse = (JavascriptExecutor) getDriver();
        jse.executeScript("browserstack_executor: {\"action\": \"setSessionName\", \"arguments\": {\"name\":\"" + testOutcome + "\" }}");
        String script = "browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"test\"}}";
        jse.executeScript(script);

        jse.executeScript(script);

    } */




    public boolean takesScreenshots() {
        return true;
    }
}