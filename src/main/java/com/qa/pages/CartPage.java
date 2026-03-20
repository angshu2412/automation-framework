package com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CartPage extends BasePage {

    private static final Logger log = LogManager.getLogger(CartPage.class);

    // Static locator
    private By checkoutBtn = By.cssSelector("a.check_out");

    // Dynamic locator methods - built at runtime with product index
    private By getProductRow(int rowIndex) {
        return By.xpath(
            String.format("//table/tbody/tr[%d]", rowIndex)
        );
    }

    private By getProductName(int rowIndex) {
        return By.xpath(
            String.format("//table/tbody/tr[%d]//td[@class='cart_description']//a", rowIndex)
        );
    }

    private By getProductPrice(int rowIndex) {
        return By.xpath(
            String.format("//table/tbody/tr[%d]/td[@class='cart_price']/p", rowIndex)
        );
    }

    private By getProductQuantity(int rowIndex) {
        return By.xpath(
            String.format("//table/tbody/tr[%d]/td[@class='cart_quantity']//button", rowIndex)
        );
    }

    private By getProductTotalPrice(int rowIndex) {
        return By.xpath(
            String.format("//table/tbody/tr[%d]/td/p[@class='cart_total_price']", rowIndex)
        );
    }

    public CartPage(WebDriver driver) {
        super(driver, 10);
    }


	public boolean isProductInCart(int index) {
       boolean isProductPresent = isDisplayed(getProductRow(index));
       log.info("Product present : {}",isProductPresent);
       return isProductPresent;
    }

    // Returns price text for product at index
    public String getPrice(int index) {
        String productPrice = getText(getProductPrice(index));
        log.info("Product price : {}",productPrice);
        return productPrice;
    }

    // Returns quantity for product at index
    public String getQuantity(int index) {
    	String productQuantity = getText(getProductQuantity(index));
        log.info("Product quantity : {}",productQuantity);
        return productQuantity;
    }

    // Returns total price for product at index
    public String getTotalPrice(int index) {
    	String productTotalPrice = getText(getProductTotalPrice(index));
        log.info("Product total price : {}",productTotalPrice);
        return productTotalPrice;
    }

    // Verifies total = price × quantity for a product
    public boolean verifyProductTotal(int index) {
        String priceText = getPrice(index).replaceAll("[^0-9.]", "");
        String totalText = getTotalPrice(index).replaceAll("[^0-9.]", "");
        String qtyText = getQuantity(index);

        double price = Double.parseDouble(priceText);
        double total = Double.parseDouble(totalText);
        int qty = Integer.parseInt(qtyText.trim());

        // Use tolerance instead of exact equality - avoids floating point issues
        boolean isCorrect = Math.abs(total - (price * qty)) < 0.01;
        log.info("Product {} total check: {} x {} = {} | expected {} → {}",
            index, price, qty, total, price * qty,
            isCorrect ? "PASS" : "FAIL");
        return isCorrect;
    }

    // Proceeds to checkout
    public CheckoutPage clickProceedToCheckout() {
        clickElement(checkoutBtn);
        log.info("Clicked on Proceed To Checkout button");
        return new CheckoutPage(driver);
    }
}