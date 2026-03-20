package com.qa.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountCreatedPage extends BasePage{
	
	private static final Logger log = LogManager.getLogger(AccountCreatedPage.class);
	
	// Locators
	private By accountCreatedSuccessMessaage = By.cssSelector("[data-qa='account-created']");
	private By continueBtn = By.cssSelector("[data-qa='continue-button']");
	
	public AccountCreatedPage(WebDriver driver) {
		super(driver, 15);
	}	
	
	public boolean isAccountCreated()
	{
		waitForVisibility(accountCreatedSuccessMessaage);
		boolean isCreated = isDisplayed(accountCreatedSuccessMessaage);
		log.info("Account created : {}",isCreated);
		return isCreated;
	}
	
	public HomePage clickOnContinueBtnAndReturnBackToHome()
	{
		waitForVisibility(continueBtn);
		clickElement(continueBtn);
		log.info("Clicked Continue button after account creation");
		return new HomePage(driver);
	}
}
