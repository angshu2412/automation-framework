package com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CheckoutPage extends BasePage {

    private static final Logger log = LogManager.getLogger(CheckoutPage.class);

    // Checkout section
    private By commentBox = By.cssSelector("[name='message']");
    private By placeOrderBtn = By.cssSelector("a[href='/payment']");
    private By checkoutInfo = By.cssSelector("[data-qa='checkout-info']");

    // Payment section
    private By nameOnCard = By.cssSelector("[data-qa='name-on-card']");
    private By cardNumber = By.cssSelector("[data-qa='card-number']");
    private By cvc = By.cssSelector("[data-qa='cvc']");
    private By expiryMonth = By.cssSelector("[data-qa='expiry-month']");
    private By expiryYear = By.cssSelector("[data-qa='expiry-year']");
    private By payBtn = By.cssSelector("[data-qa='pay-button']");

    public CheckoutPage(WebDriver driver) {
        super(driver, 10);
    }

    // Verifies checkout page loaded
    public boolean isCheckoutPageLoaded() {
        boolean isPageLoaded = isDisplayed(checkoutInfo);
        log.info("Checkout page displayed : {}",isPageLoaded);
        return isPageLoaded;
    }

    // Adds comment and clicks place order
    public void placeOrder(String comment) {
        typeToTextBox(commentBox, comment);
        clickElement(placeOrderBtn);
        log.info("Placed order with comment: {}", comment);
    }

    // Fills payment details and confirms order
    public OrderConfirmedPage fillPaymentDetails(String name, String number,
            String cvcValue, String month, String year) {
        typeToTextBox(nameOnCard, name);
        typeToTextBox(cardNumber, number);
        typeToTextBox(cvc, cvcValue);
        typeToTextBox(expiryMonth, month);
        typeToTextBox(expiryYear, year);
        clickElement(payBtn);
        log.info("Payment submitted for card holder: {}", name);
        return new OrderConfirmedPage(driver);
    }
}