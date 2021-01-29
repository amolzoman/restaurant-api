package com.zomato.restaurantapi.kafka.service;

import com.zomato.restaurantapi.kafka.producer.RestaurantProducer;
import com.zomato.restaurantapi.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantProducerServiceImpl implements RestaurantKafkaService {
    @Autowired
    private RestaurantProducer restaurantProducer;

    @Override
    public void handleAddNewRestaurant(Restaurant restaurant) {
        restaurantProducer.produce(restaurant);
    }
}
