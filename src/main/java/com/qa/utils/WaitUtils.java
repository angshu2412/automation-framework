package com.qa.utils;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtils {

    private static final Logger log = LogManager.getLogger(WaitUtils.class);

    // Default timeout used across all methods
    private static final int DEFAULT_TIMEOUT = 10;

    // Private constructor - nobody should instantiate this class
    // All methods are static, so no object is ever needed
    private WaitUtils() {}

    // Waits until element is visible on screen
    public static WebElement waitForVisibility(WebDriver driver, By locator) {
        log.info("Waiting for element to be visible: {}", locator);
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT))
            .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Waits until element is clickable (visible + enabled)
    public static WebElement waitForClickability(WebDriver driver, By locator) {
        log.info("Waiting for element to be clickable: {}", locator);
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT))
            .until(ExpectedConditions.elementToBeClickable(locator));
    }

    // Waits until element disappears
    public static boolean waitForInvisibility(WebDriver driver, By locator) {
        log.info("Waiting for element to disappear: {}", locator);
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT))
            .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // Waits until page URL contains expected text
    public static boolean waitForUrlToContain(WebDriver driver, String partialUrl) {
        log.info("Waiting for URL to contain: {}", partialUrl);
        return new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT))
            .until(ExpectedConditions.urlContains(partialUrl));
    }

    // Waits until browser finishes loading the page completely
    public static void waitForPageLoad(WebDriver driver) {
        log.info("Waiting for page to fully load");
        new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT))
            .until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState")
                .equals("complete"));
    }

    // Waits with a custom timeout - for elements that take longer
    public static WebElement waitForVisibility(
            WebDriver driver, By locator, int timeoutSeconds) {
        log.info("Waiting {}s for element: {}", timeoutSeconds, locator);
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
            .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}