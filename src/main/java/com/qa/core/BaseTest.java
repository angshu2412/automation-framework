package com.qa.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.qa.config.ConfigReader;

public class BaseTest {
	
	private static final Logger log = LogManager.getLogger(BaseTest.class);
	
	@BeforeMethod
	public void setupDriver()
	{
        String browser = ConfigReader.getInstance().get("browser");
        boolean headless = Boolean.parseBoolean(
            ConfigReader.getInstance().get("headless")
        );

        log.info("Initialising browser: {} | headless: {}", browser, headless);
        DriverFactory.initDriver(browser, headless);
        log.info("Browser initialised successfully");
	}
	
	@AfterMethod
	public void tearDown()
	{
		 log.info("Quitting browser");
	     DriverFactory.quitDriver();
	}
}
