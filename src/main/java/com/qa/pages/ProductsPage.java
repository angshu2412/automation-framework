package com.qa.pages;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.qa.utils.WaitUtils;

public class ProductsPage extends BasePage {

    private static final Logger log = LogManager.getLogger(ProductsPage.class);

    // Locators
    private By searchInput = By.cssSelector("#search_product");
    private By searchBtn = By.cssSelector("button#submit_search");
    private By productCards = By.cssSelector(".single-products");
    private By addToCartBtn = By.cssSelector(".productinfo a.add-to-cart");
    private By continueShoppingBtn = By.cssSelector("[data-dismiss='modal']");
    private By cartLink = By.cssSelector("a[href='/view_cart']");
    private By category = By.xpath("//p[contains(text(),'Category')]");
    
    private By getViewProductLink(int index) {
        return By.cssSelector(
        		String.format("a[href='/product_details/%d']", index + 1)
        );
    }

    public void clickOnViewProductFor(int index)
    {
    	removeAds();
    	WaitUtils.waitForPageLoad(driver); 
    	By locator = getViewProductLink(index);
    	// Retry up to 3 times if element goes stale
        int attempts = 0;
        while (attempts < 3) {
            try {
                // Scroll element into view first - avoids ads blocking click
                WebElement element = wait.until(
                    ExpectedConditions.elementToBeClickable(locator)
                );
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView(true);", element
                );
                // Small pause for scroll to settle
                ((JavascriptExecutor) driver).executeScript(
                    "window.scrollBy(0, -100);"  // scroll back slightly so header doesn't cover it
                );
                element.click();
                log.info("Clicked view product for index: {}", index);
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                log.warn("Stale element on attempt {} for index {}", attempts, index);
            }
        }
        throw new RuntimeException("Could not click view product after 3 attempts");
    }
    
    public String getProductCategory()
    {
    	String fullText = getText(category);
        log.info("Full category text: {}", fullText);
        // fullText looks like "Category: Women > Tops"
        // Split on ":" → ["Category", " Women > Tops"]
        // Then split on ">" → [" Women ", " Tops"]
        // We want "Tops" - the actual category name
        if (fullText.contains(":") && fullText.contains(">")) {
            String afterColon = fullText.split(":")[1];     // " Women > Tops"
            String afterArrow = afterColon.split(">")[1];   // " Tops"
            return afterArrow.trim();                        // "Tops"
        }
        return fullText.trim();
    }
    
    public ProductsPage(WebDriver driver) {
        super(driver, 10);
    }

    // Types search term and clicks search button
    public void searchProduct(String productName) {
        typeToTextBox(searchInput, productName);
        clickElement(searchBtn);
        log.info("Searched for product: {}", productName);
        closeAdIfPresent();
    }

    // Adds product at given index (0=first, 1=second) to cart
    public void addProductToCart(int index) {
        // Get all matching add-to-cart buttons
    	waitForVisibility(addToCartBtn);
        List<WebElement> addToCartBtns = driver.findElements(addToCartBtn);

        // Make sure index exists in results
        if (index >= addToCartBtns.size()) {
            throw new RuntimeException(
                "Product index " + index + " not found. Only "
                + addToCartBtns.size() + " products available"
            );
        }

        addToCartBtns.get(index).click();
        log.info("Clicked add to cart for product at index: {}", index);

        // Wait for modal and dismiss it to continue shopping
        waitForVisibility(continueShoppingBtn);
        clickElement(continueShoppingBtn);
        log.info("Dismissed modal - continuing shopping");
    }

    // Gets name of product at given index - used for API vs UI validation
    public String getProductName(int index) {
        List<WebElement> products = driver.findElements(productCards);
        String name = products.get(index)
            .findElement(By.cssSelector(".productinfo p"))
            .getText();
        log.info("Product name at index {}: {}", index, name);
        return name;
    }

    // Gets price of product at given index
    public String getProductPrice(int index) {
        List<WebElement> products = driver.findElements(productCards);
        String price = products.get(index)
            .findElement(By.cssSelector(".productinfo h2"))
            .getText();
        log.info("Product price at index {}: {}", index, price);
        return price;
    }

    // Navigates to cart after adding products
    public CartPage goToCart() {
        clickElement(cartLink);
        log.info("Navigating to cart");
        return new CartPage(driver);
    }
}