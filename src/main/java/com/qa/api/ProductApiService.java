package com.qa.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.models.Product;
import com.qa.models.ProductResponse;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductApiService extends ApiClient {

    private static final Logger log = LogManager.getLogger(ProductApiService.class);

    public ProductResponse getProducts(){
    	log.info("Fetching products from: {}/api/productsList", baseUrl);
        try {
            String responseBody = getRequestSpec()
                .when()
                    .get("/api/productsList")
                .then()
                    .statusCode(200)
                    .extract()
                    .asString();

            log.info("Products API responded successfully");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(responseBody, ProductResponse.class);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse products API response", e);
        }
    }
    
    // Returns a single product by index from the list
    // Used for API vs UI validation in Part 2 of assignment
    public Product getProductByIndex(int index) {
        ProductResponse response = getProducts();
        if (index >= response.getProducts().size()) {
            throw new RuntimeException(
                "Product index " + index + " not found in API response"
            );
        }
        Product product = response.getProducts().get(index);
        log.info("Retrieved product: {}", product);
        return product;
    }
}