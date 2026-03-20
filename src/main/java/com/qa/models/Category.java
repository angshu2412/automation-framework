package com.qa.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {

    private UserType usertype;
    private String category;

    public Category() {}

    public UserType getUsertype() { return usertype; }
    public String getCategory() { return category; }
}