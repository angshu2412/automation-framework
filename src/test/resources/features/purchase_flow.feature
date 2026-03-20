Feature: User Purchase Flow
  As a new user
  I want to register, purchase products and delete my account
  So that I can validate the end to end purchase flow

@regression @ui
 Scenario: End to end purchase flow with account creation and deletion
    Given the application is launched
    When I register as a new user
    And I login with the registered user credentials
    Then I verify that I am logged in successfully
    When I search for a product "Top"
    And I add the first product to cart
    And I add the second product to cart
    And I navigate to the cart
    Then I verify the cart total is calculated correctly
    When I proceed to checkout
    And I place the order with comment "Please deliver fast"
    And I fill in the payment details
    Then I verify the order is placed successfully
    When I delete my account
    Then I verify the account is deleted successfully