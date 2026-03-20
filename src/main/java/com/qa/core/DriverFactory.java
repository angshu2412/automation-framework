package com.qa.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	
	public static void initDriver(String browser, boolean headless)
	{
		// System property overrides config.properties
	    // Allows Docker/CI to pass -Dheadless=true without changing config file
	    String headlessProp = System.getProperty("headless");
	    if (headlessProp != null) {
	        headless = Boolean.parseBoolean(headlessProp);
	    }

	    String browserProp = System.getProperty("browser");
	    if (browserProp != null) {
	        browser = browserProp;
	    }
		
		switch(browser.toLowerCase())
		{
		case "chrome":
			// Step 1: download correct chromedriver binary
            WebDriverManager.chromedriver().setup();

            // Step 2: configure browser options
            ChromeOptions chromeOptions = new ChromeOptions();
            if (headless) {
                chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
            }
            chromeOptions.addArguments("--window-size=1920,1080");

            // Step 3: store in ThreadLocal so each thread has its own instance
            driver.set(new ChromeDriver(chromeOptions));
            break;
            
		case "edge":
            WebDriverManager.edgedriver().setup();

            EdgeOptions edgeOptions = new EdgeOptions();
            if (headless) {
            	edgeOptions.addArguments("--headless=new");
            	edgeOptions.addArguments("--no-sandbox");
            	edgeOptions.addArguments("--disable-dev-shm-usage");
            }
            
            driver.set(new EdgeDriver(edgeOptions));
            break;
		case "firefox":
			WebDriverManager.firefoxdriver().setup();

            FirefoxOptions firefoxOptions = new FirefoxOptions();
            if (headless) {
                firefoxOptions.addArguments("--headless");
            }

            driver.set(new FirefoxDriver(firefoxOptions));
            break;
			
		default:
            throw new RuntimeException("Browser not supported: " + browser);
		}
	}
	
	public static WebDriver getDriver()
	{
		return driver.get();
	}
	
	public static void quitDriver()
	{
		if (driver.get() != null) {
            driver.get().quit();
            driver.remove(); // cleans up thread's reference
        }
	}
}
