package com.zomato.restaurantapi.kafka.service;

import com.zomato.restaurantapi.model.Restaurant;
import com.zomato.restaurantapi.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantConsumerServiceImpl implements RestaurantKafkaService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public void handleAddNewRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }
}
