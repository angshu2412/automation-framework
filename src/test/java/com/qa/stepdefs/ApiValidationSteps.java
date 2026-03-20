package com.qa.stepdefs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.qa.api.ProductApiService;
import com.qa.config.ConfigReader;
import com.qa.core.DriverFactory;
import com.qa.models.Category;
import com.qa.models.Product;
import com.qa.pages.HomePage;
import com.qa.pages.ProductsPage;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ApiValidationSteps {
	
	private static final Logger log = LogManager.getLogger(ApiValidationSteps.class);
	
	// Fields
	private ProductApiService productApiService = new ProductApiService();
	private Product apiProduct;  // stores fetched product for reuse across steps
	private ProductsPage productsPage;
	private HomePage homePage;
	private int currentIndex;

	// Steps matching your feature file exactly
	@Given("I fetch product at index {int} from the API")
	public void fetchProductFromApi(int index) { 
		 apiProduct = productApiService.getProductByIndex(index);
	     currentIndex = index;
	     log.info("Fetched API product: {}", apiProduct);
	}

	@When("I navigate to the products page")
	public void navigateToProductsPage() { 
		WebDriver driver = DriverFactory.getDriver();
		homePage = new HomePage(driver);
		homePage.navigateToProductsPage(); 
		productsPage = new ProductsPage(driver);
        log.info("Navigated to products page");
		
	}

	@Then("the product name should match the API response")
	public void verifyProductName() {
		
		String uiName = productsPage.getProductName(currentIndex);
        String apiName = apiProduct.getName();
        log.info("UI name: {} | API name: {}", uiName, apiName);
        Assert.assertEquals(uiName, apiName,
            "Product name mismatch between UI and API");
	}

	@Then("the product price should match the API response")
	public void verifyProductPrice() { 
		String uiPrice = productsPage.getProductPrice(currentIndex);
        String apiPrice = apiProduct.getPrice();
        log.info("UI price: {} | API price: {}", uiPrice, apiPrice);
        Assert.assertEquals(uiPrice, apiPrice,
            "Product price mismatch between UI and API");
	}

	@Then("the product category should match the API response")
	public void verifyProductCategory() { 
		
		productsPage.clickOnViewProductFor(currentIndex);
	    String uiCategory = productsPage.getProductCategory();
	    String apiCategory = apiProduct.getCategory().getCategory();
	    log.info("UI category: {} | API category: {}", uiCategory, apiCategory);

	    Assert.assertEquals(uiCategory, apiCategory,
	        "Product category mismatch between UI and API");
	}
}
