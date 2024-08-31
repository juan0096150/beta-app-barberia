package com.example.barberia.model;

public class Service {
    private int service_id;
    private String name;
    private String description;
    private int duration_minutes;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration_minutes() {
        return duration_minutes;
    }

    public void setDuration_minutes(int duration_minutes) {
        this.duration_minutes = duration_minutes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public Service(String description, int duration_minutes, String name, String price, int service_id) {
        this.description = description;
        this.duration_minutes = duration_minutes;
        this.name = name;
        this.price = price;
        this.service_id = service_id;
    }

    private String price;

    // Constructor, getters y setters

    @Override
    public String toString() {
        return name + " - $" + price;
    }
}