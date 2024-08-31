package com.example.barberia.model;

public class LoginResponse {
    public LoginResponse(Customer customer, String token) {
        this.customer = customer;
        this.token = token;
    }

    private String token;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private Customer customer;

    // Constructor, getters y setters
}
