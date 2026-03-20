package com.qa.api;

import com.qa.config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiClient {

    protected String baseUrl = ConfigReader.getInstance().get("base.url");

    protected RequestSpecification getRequestSpec() {
        return RestAssured.given().baseUri(baseUrl)
        		.contentType(ContentType.JSON)
        		.log().ifValidationFails();
    }
}