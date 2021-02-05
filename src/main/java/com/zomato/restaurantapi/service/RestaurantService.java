package com.zomato.restaurantapi.service;

import com.zomato.restaurantapi.model.Restaurant;

import java.util.List;

public interface RestaurantService {

    List<Restaurant> findAll();

    void save(Restaurant restaurant);

    Restaurant findById(Long id);

    Restaurant findByName(String name);

    List<Restaurant> findByDineInAvailability(boolean dineInAvailable);

    void removeById(Long id);

    void updateSeats(Long id, int numberOfSeats);

    void update(Long id, Restaurant restaurant);
}
