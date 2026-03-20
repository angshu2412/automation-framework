package com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.utils.TestDataGenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegisterPage extends BasePage {

    private static final Logger log = LogManager.getLogger(RegisterPage.class);

    private By titleMr = By.cssSelector("#id_gender1");
    private By titleMrs = By.cssSelector("#id_gender2");
    private By passwordInputField = By.cssSelector("[data-qa='password']");
    private By days = By.cssSelector("[data-qa='days']");
    private By months = By.cssSelector("[data-qa='months']");
    private By years = By.cssSelector("[data-qa='years']");
    private By firstNameInputField = By.cssSelector("[data-qa='first_name']");
    private By lastNameInputField = By.cssSelector("[data-qa='last_name']");
    private By address1 = By.cssSelector("#address1");
    private By countryDropdown = By.cssSelector("#country");
    private By stateInputField = By.cssSelector("[data-qa='state']");
    private By cityInputField = By.cssSelector("[data-qa='city']");
    private By zipcodeInputField = By.cssSelector("[data-qa='zipcode']");
    private By mobileNumberInputField = By.cssSelector("[data-qa='mobile_number']");
    private By createAccountBtn = By.cssSelector("[data-qa='create-account']");

    public RegisterPage(WebDriver driver) {
        super(driver, 10);
    }

    // Fills entire registration form and submits
    public AccountCreatedPage fillRegistrationForm(String password,
            String firstName, String lastName, String address,String country,
            String state, String city, String zip, String mobile) {

        // Click Mr title
    	waitForVisibility(titleMr);
        clickElement(titleMr);
        log.info("Selected title: Mr");

        // Type password
        typeToTextBox(passwordInputField, password);

        // Select date of birth using Select class
        selectDropdownByValue(days, TestDataGenerator.generateDobDay());
        selectDropdownByValue(months, TestDataGenerator.generateDobMonth());
        selectDropdownByValue(years, TestDataGenerator.generateDobYear());

        // Fill personal details - complete these yourself
        typeToTextBox(firstNameInputField, firstName);
        typeToTextBox(lastNameInputField, lastName);
        typeToTextBox(address1, address);

        // Select country
        selectDropdownByVisibleText(countryDropdown,country);

        // Fill remaining fields
        typeToTextBox(stateInputField, state);
        typeToTextBox(cityInputField, city);
        typeToTextBox(zipcodeInputField, zip);
        typeToTextBox(mobileNumberInputField, mobile);

        // Submit form
        closeAdIfPresent();
        clickElement(createAccountBtn);
        return new AccountCreatedPage(driver);
    }
}