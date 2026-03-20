package com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginPage extends BasePage {

    private static final Logger log = LogManager.getLogger(LoginPage.class);

    // Login section locators
    private By loginEmail = By.cssSelector("[data-qa='login-email']");
    private By loginPassword = By.cssSelector("[data-qa='login-password']");
    private By loginButton = By.cssSelector("[data-qa='login-button']");

    // Signup section locators
    private By signupName = By.cssSelector("[data-qa='signup-name']");
    private By signupEmail = By.cssSelector("[data-qa='signup-email']");
    private By signupButton = By.cssSelector("[data-qa='signup-button']");

    public LoginPage(WebDriver driver) {
        super(driver, 10);
    }

    // Login method - returns HomePage on success
    public HomePage login(String email, String password) {
        log.info("Logging in with email: {}", email);
        typeToTextBox(loginEmail, email);
        typeToTextBox(loginPassword, password);
        clickElement(loginButton);
        return new HomePage(driver);
    }

    // Signup method - returns next page (RegisterPage)
    public RegisterPage enterSignupDetails(String name, String email) {
        log.info("Entering signup details for: {}", name);
        typeToTextBox(signupName, name);
        typeToTextBox(signupEmail, email);
        closeAdIfPresent();
        clickElement(signupButton);
        return new RegisterPage(driver);
    }
}