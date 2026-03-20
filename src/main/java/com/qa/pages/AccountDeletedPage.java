package com.qa.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountDeletedPage extends BasePage{
	
	private static final Logger log = LogManager.getLogger(AccountDeletedPage.class);
	
	public AccountDeletedPage(WebDriver driver) {
		super(driver, 10);
	}
	
	// Locators
	private By deletedAccountSuccessMsg = By.cssSelector("[data-qa='account-deleted']");
	private By continueBtn = By.cssSelector("[data-qa='continue-button']");
	
	public boolean isAccountDeleted() {
		boolean isDeleted = isDisplayed(deletedAccountSuccessMsg);
		log.info("Account deleted : {}",isDeleted);
		return isDeleted;
	}
	
	public HomePage clickOnContinueBtnToReturnToHomePage()
	{
		clickElement(continueBtn);
		log.info("Clicked on continue button after account deletion");
		return new HomePage(driver);
	}
}
