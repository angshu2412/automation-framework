package com.qa.stepdefs;

import com.qa.core.DriverFactory;
import com.qa.pages.*;
import com.qa.utils.TestDataGenerator;

import io.cucumber.java.en.*;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class PurchaseSteps {

    // Page objects - initialized lazily when needed
    private HomePage homePage;
    private LoginPage loginPage;
    private RegisterPage registerPage;
    private AccountCreatedPage accountCreatedPage;
    private ProductsPage productsPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private OrderConfirmedPage orderConfirmedPage;
    private AccountDeletedPage accountDeletedPage;

    // Helper to get driver cleanly
    private WebDriver driver() {
        return DriverFactory.getDriver();
    }

    @Given("the application is launched")
    public void applicationIsLaunched() {
        homePage = new HomePage(driver());
        Assert.assertTrue(homePage.isHomePageLoaded(),
            "Home page did not load");
    }

    @When("I register as a new user")
    public void registerNewUser() {
        // Generate all data upfront - stored in ThreadLocal for reuse
        String name = TestDataGenerator.generateName();
        String email = TestDataGenerator.generateEmail();
        String password = TestDataGenerator.generatePassword();
        String firstName = TestDataGenerator.generateFirstName();
        String lastName = TestDataGenerator.generateLastName();
        String address = TestDataGenerator.generateAddress();
        String state = TestDataGenerator.generateState();
        String city = TestDataGenerator.generateCity();
        String zip = TestDataGenerator.generateZip();
        String mobile = TestDataGenerator.generateMobile();

        loginPage = homePage.clickSignupLogin();
        
        registerPage = loginPage.enterSignupDetails(name, email);
        accountCreatedPage = registerPage.fillRegistrationForm(
            password,
            firstName,
            lastName,
            address,
            "India",    // country stays fixed - dropdown value
            state,
            city,
            zip,
            mobile
        );

        Assert.assertTrue(accountCreatedPage.isAccountCreated(),
            "Account creation failed");
        homePage = accountCreatedPage.clickOnContinueBtnAndReturnBackToHome();
        homePage.clickOnLogout();
    }

    @When("I login with the registered user credentials")
    public void loginWithCreatedUser() {
        loginPage = homePage.clickSignupLogin();
        homePage = loginPage.login(
            TestDataGenerator.getCurrentEmail(),
            TestDataGenerator.getCurrentPassword()
        );
    }

    @Then("I verify that I am logged in successfully")
    public void verifyLoggedIn() {
        Assert.assertTrue(homePage.isLoggedIn(),
            "User is not logged in");
    }

    @When("I search for a product {string}")
    public void searchForProduct(String productName) {
        // Navigate to products page first
    	homePage.navigateToProductsPage();
        productsPage = new ProductsPage(driver());
        productsPage.searchProduct(productName);
    }

    @When("I add the first product to cart")
    public void addFirstProduct() {
    	productsPage.removeAds();
        productsPage.addProductToCart(0);
    }

    @When("I add the second product to cart")
    public void addSecondProduct() {
        productsPage.addProductToCart(1);
    }

    @When("I navigate to the cart")
    public void navigateToCart() {
        cartPage = productsPage.goToCart();
    }

    @Then("I verify the cart total is calculated correctly")
    public void verifyCartTotal() {
        Assert.assertTrue(cartPage.verifyProductTotal(1),
            "Cart total for product 1 is incorrect");
        Assert.assertTrue(cartPage.verifyProductTotal(2),
            "Cart total for product 2 is incorrect");
    }

    @When("I proceed to checkout")
    public void proceedToCheckout() {
        checkoutPage = cartPage.clickProceedToCheckout();
        Assert.assertTrue(checkoutPage.isCheckoutPageLoaded(),
            "Checkout page did not load");
    }

    @When("I place the order with comment {string}")
    public void placeOrder(String comment) {
        checkoutPage.placeOrder(comment);
    }

    @When("I fill in the payment details")
    public void fillPaymentDetails() {
        orderConfirmedPage = checkoutPage.fillPaymentDetails(
            "Test User",        // name on card
            "4111111111111111", // test visa card number
            "123",              // cvc
            "12",               // expiry month
            "2027"              // expiry year
        );
    }

    @Then("I verify the order is placed successfully")
    public void verifyOrderPlaced() {
        Assert.assertTrue(orderConfirmedPage.isOrderPlaced(),
            "Order was not placed successfully");
    }

    @When("I delete my account")
    public void deleteAccount() {
        accountDeletedPage = homePage.deleteAccount();
    }

    @Then("I verify the account is deleted successfully")
    public void verifyAccountDeleted() {
        Assert.assertTrue(accountDeletedPage.isAccountDeleted(),
            "Account deletion failed");
        accountDeletedPage.clickOnContinueBtnToReturnToHomePage();
    }
}