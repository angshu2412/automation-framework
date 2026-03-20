package com.qa.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage{
	
	private static final Logger log = LogManager.getLogger(HomePage.class);
	
	// Locators
	private By signupLoginBtn = By.cssSelector("a[href='/login']");
	private By logo = By.cssSelector("div.logo img");
	private By deleteAccountLink = By.cssSelector("a[href='/delete_account']");
	private By loggedInUser = By.xpath("//i[@class='fa fa-user']/parent::a/b");
	private By productsNavigationLink = By.cssSelector("a[href='/products']");
	private By logoutLink = By.cssSelector("a[href='/logout']");
			
	public HomePage(WebDriver driver) {
        super(driver, 10);
    }
	
	public boolean isHomePageLoaded() {
        return isDisplayed(logo); 
    }
	
	public void navigateToProductsPage()
	{
		clickElement(productsNavigationLink);
		log.info("Clicked on products navigation link");
	}
	
	public void clickOnLogout()
	{
		log.info("Clicking Logout button");
        clickElement(logoutLink);
	}
	
	public LoginPage clickSignupLogin() {
		log.info("Clicking Signup/Login button");
        clickElement(signupLoginBtn);
        return new LoginPage(driver);
    }
	
	public boolean isLoggedIn() { 
		boolean amILoggedIn = isDisplayed(loggedInUser);
		log.info("Logged in state : {}",amILoggedIn);
		return amILoggedIn;
	}
	
	public AccountDeletedPage deleteAccount() { 
		clickElement(deleteAccountLink);
		log.info("Clicked on delete account link");
		return new AccountDeletedPage(driver);
	}
}
