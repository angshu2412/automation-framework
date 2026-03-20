package com.qa.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Tells Jackson to ignore any JSON fields not mapped here
// Prevents failure if API adds new fields later
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    private int id;
    private String name;
    private String price;
    private String brand;
    private Category category;

    // Jackson needs empty constructor to create object before setting fields
    public Product() {}

    // Getters - Jackson uses these to read values after deserialization
    public int getId() { return id; }
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getBrand() { return brand; }
    public Category getCategory() { return category; }

    @Override
    public String toString() {
        return String.format("Product{id=%d, name='%s', price='%s', brand='%s'}",
            id, name, price, brand);
    }
}