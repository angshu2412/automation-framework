package com.qa.pages;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class BasePage {
	protected WebDriver driver;
	protected WebDriverWait wait;
	
	private static final Logger log = LogManager.getLogger(BasePage.class);
	
	public BasePage(WebDriver driver,int timeoutInSeconds)
	{
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
	}
	
	public void removeAds() {
	    try {
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript(
	            "var selectors = [" +
	            "  'iframe[id^=\"aswift\"]'," +    // Google aswift iframes
	            "  'iframe[title=\"Advertisement\"]'," + // ad iframes by title
	            "  'ins.adsbygoogle'," +            // adsense containers
	            "  'div#ad_position_box'," +        // ad position boxes
	            "  'div.adsbygoogle'" +             // ad divs
	            "];" +
	            "selectors.forEach(function(sel) {" +
	            "  document.querySelectorAll(sel).forEach(function(el) {" +
	            "    el.remove();" +
	            "  });" +
	            "});"
	        );
	        log.info("Removed ad elements from DOM");
	    } catch (Exception e) {
	        log.info("No ads to remove: {}", e.getMessage());
	    }
	}
	
	public void closeAdIfPresent() {
	    try {
	        // Step 1 - find the shadow host element
	        WebElement shadowHost = driver.findElement(
	            By.cssSelector("div.grippy-host")
	        );
	        log.info("Shadow host found: {}", shadowHost.isDisplayed());

	        
	        // Step 2 - get shadow root as SearchContext (Selenium 4 native)
	        SearchContext shadowRoot = shadowHost.getShadowRoot();
	        log.info("Shadow root obtained: {}", shadowRoot);
	        // Step 3 - search inside shadow root for the path/close element
	        WebElement closeBtn = shadowRoot.findElement(
	            By.cssSelector("path")
	        );

	        if (closeBtn != null && closeBtn.isDisplayed()) {
	            closeBtn.click();
	            log.info("Closed ad via shadow DOM SearchContext");
	        }

	    } catch (NoSuchElementException e) {
	        log.info("No ad present - continuing");
	    } catch (Exception e) {
	        log.info("Ad dismissal skipped: {}", e.getMessage());
	    }
	}
	
	public void clickElement(By locator) {
		wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
		log.info("Clicked on element with locator : {} successfully",locator);
	}
	
	public void typeToTextBox(By locator,String textToBeTyped) {
		wait.until(ExpectedConditions.elementToBeClickable(locator)).clear();
		driver.findElement(locator).sendKeys(textToBeTyped);
		log.info("Typed on element with locator : {} successfully",locator);
	}
	
	public String getText(By locator) {
		 String text = wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
		 log.info("Got text '{}' from element: {}", text, locator);
		 return text;
	}

	public boolean isDisplayed(By locator) {
		 try {
	            return driver.findElement(locator).isDisplayed();
	        } catch (Exception e) {
	            // element not found
	            return false;
	        }
	}
	
	public void waitForVisibility(By locator)
	{
		wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		log.info("Element visible: {}", locator);
	}
	
	public void navigateTo(String url)
	{
		driver.get(url);
		log.info("Navigated to : {} successfully",url);
	}
	
	public String getPageTitle()
	{
		String title = driver.getTitle();
        log.info("Page title: {}", title);
        return title;
	}
	
	public void selectDropdownByIndex(By locator,int index)
	{
		Select select = new Select(driver.findElement(locator));
		select.selectByIndex(index);
		log.info("Selected : {} by index for dropdown having : {}",index,locator);
	}
	
	public void selectDropdownByVisibleText(By locator,String visibleText)
	{
		Select select = new Select(driver.findElement(locator));
		select.selectByVisibleText(visibleText);
		log.info("Selected : {} by visible text for dropdown having : {}",visibleText,locator);
	}
	
	public void selectDropdownByValue(By locator,String optionValue)
	{
		Select select = new Select(driver.findElement(locator));
		select.selectByValue(optionValue);
		log.info("Selected : {} by option value for dropdown having : {}",optionValue,locator);
	}
}
