package com.qa.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger log = LogManager.getLogger(RetryAnalyzer.class);

    // Current attempt counter - starts at 0
    private int retryCount = 0;

    // Maximum number of retries allowed
    private static final int MAX_RETRY = 3;

    // TestNG calls this method after every failure
    // Return true = retry the test
    // Return false = give up, mark as failed
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY) {
            retryCount++;
            log.warn("Retrying test '{}' - attempt {} of {}",
                result.getName(), retryCount, MAX_RETRY);
            return true;  // try again
        }
        log.error("Test '{}' failed after {} retries",
            result.getName(), MAX_RETRY);
        return false; // give up
    }
}