package com.qa.models;

import java.util.List;

public class ProductResponse {
	private int responseCode;
	private List<Product> products;
	
	public ProductResponse() { }
	
	public int getResponseCode()
	{
		return responseCode;
	}
	
	public List<Product> getProducts()
	{
		return products;
	}
}
