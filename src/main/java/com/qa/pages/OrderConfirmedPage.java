package com.qa.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrderConfirmedPage extends BasePage{

	public OrderConfirmedPage(WebDriver driver) {
		super(driver,10);
	}
	
	private static final Logger log = LogManager.getLogger(OrderConfirmedPage.class);
	
	// Locators
	private By orderPlacedSuccessMsg = By.cssSelector("[data-qa='order-placed']");
	
	public boolean isOrderPlaced() {
		boolean isPlaced = isDisplayed(orderPlacedSuccessMsg);
		log.info("Order placed : {}",isPlaced);
		return isPlaced;
	}
}
