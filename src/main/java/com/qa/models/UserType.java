package com.qa.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserType {

    private String usertype;

    public UserType() {}

    public String getUsertype() { return usertype; }
}