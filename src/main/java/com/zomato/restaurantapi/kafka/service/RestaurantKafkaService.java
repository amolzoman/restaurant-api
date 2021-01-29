package com.zomato.restaurantapi.kafka.service;

import com.zomato.restaurantapi.model.Restaurant;

public interface RestaurantKafkaService {
    void handleAddNewRestaurant(Restaurant restaurant);
}
