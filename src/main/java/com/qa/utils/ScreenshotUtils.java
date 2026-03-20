package com.qa.utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtils {

    private static final Logger log = LogManager.getLogger(ScreenshotUtils.class);

    // Nobody should instantiate this utility class
    private ScreenshotUtils() {}

    // Returns screenshot as byte array
    public static byte[] captureScreenshotAsBytes(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.error("Failed to capture screenshot as bytes: {}", e.getMessage());
            return new byte[0];
        }
    }

    // Saves screenshot to /screenshots folder with timestamp in filename
    public static String captureScreenshotAsFile(WebDriver driver, String scenarioName) {
        try {
            // Cast driver to TakesScreenshot
            File screenshot = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE);

            // Build unique filename using scenario name + timestamp
            String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            // Sanitize scenario name
            String safeName = scenarioName.replaceAll("[^a-zA-Z0-9]", "_");
            String fileName = safeName + "_" + timestamp + ".png";
            String filePath = "screenshots/" + fileName;

            // Copy from temp location to our screenshots folder
            FileUtils.copyFile(screenshot, new File(filePath));
            log.info("Screenshot saved: {}", filePath);

            return filePath;

        } catch (IOException e) {
            log.error("Failed to save screenshot: {}", e.getMessage());
            return null;
        }
    }
}