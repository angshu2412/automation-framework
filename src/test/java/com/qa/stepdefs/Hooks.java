package com.qa.stepdefs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.qa.config.ConfigReader;
import com.qa.core.DriverFactory;
import com.qa.utils.ScreenshotUtils;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;

public class Hooks {

    private static final Logger log = LogManager.getLogger(Hooks.class);

    @Before
    public void setUp(Scenario scenario) {
        // Read config and initialise browser before each scenario
        String browser = ConfigReader.getInstance().get("browser");
        boolean headless = Boolean.parseBoolean(
            ConfigReader.getInstance().get("headless")
        );

        log.info("Starting scenario: {}", scenario.getName());
        DriverFactory.initDriver(browser, headless);

        // Navigate to base URL at start of every scenario
        String baseUrl = ConfigReader.getInstance().get("base.url");
        DriverFactory.getDriver().get(baseUrl);
        DriverFactory.getDriver().manage().window().maximize();
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverFactory.getDriver();

        if (scenario.isFailed() && driver != null) {
            log.warn("Scenario FAILED: {} - capturing screenshot", scenario.getName());

            // Capture as bytes and attach to Allure report
            byte[] screenshot = ScreenshotUtils.captureScreenshotAsBytes(driver);

            // Attach to Allure report
            Allure.addAttachment(
                "Screenshot - " + scenario.getName(),
                new ByteArrayInputStream(screenshot)
            );

            // Also save to file for local debugging
            ScreenshotUtils.captureScreenshotAsFile(driver, scenario.getName());
        }

        log.info("Scenario {}: {}", 
            scenario.isFailed() ? "FAILED" : "PASSED", 
            scenario.getName()
        );

        DriverFactory.quitDriver();
    }
}