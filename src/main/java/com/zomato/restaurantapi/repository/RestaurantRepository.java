package com.zomato.restaurantapi.repository;

import com.zomato.restaurantapi.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByName(String name);
    Optional<List<Restaurant>> findByIsDineInAvailable(boolean dineInAvailable);
}
