Feature: Product API Validation
  As a QA engineer
  I want to verify product data consistency between API and UI
  So that I can ensure the frontend displays accurate product information

@api
  Scenario: Verify first product details match between API and UI
    Given I fetch product at index 0 from the API
    When I navigate to the products page
    Then the product name should match the API response
    And the product price should match the API response
    And the product category should match the API response