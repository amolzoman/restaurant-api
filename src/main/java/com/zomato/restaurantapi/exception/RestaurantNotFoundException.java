package com.zomato.restaurantapi.exception;

public class RestaurantNotFoundException extends RuntimeException {
    private static final String EXCEPTION_PREFIX = "Could not find Restaurant with ";

    public <T> RestaurantNotFoundException(String field, T information) {
        super(EXCEPTION_PREFIX + field + " " + information);
    }

    public RestaurantNotFoundException(Long id) {
        this("id", id);
    }

    public RestaurantNotFoundException(boolean dineInAvailable) {
        this("dine in available as", dineInAvailable);
    }

    public RestaurantNotFoundException(String name) {
        this("name", name);
    }
}
