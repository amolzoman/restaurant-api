package com.zomato.restaurantapi.controller;

import com.zomato.restaurantapi.exception.RestaurantNotFoundException;
import com.zomato.restaurantapi.model.Restaurant;
import com.zomato.restaurantapi.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
public class RestaurantController {
    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping(path = "/restaurants")
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @GetMapping(path = "/restaurants/{id}")
    public Restaurant getRestaurantById(@PathVariable Long id) {
        return getRestaurantByIdOrThrow(id);
    }

    @GetMapping(path = "/restaurants", params = "name")
    public Restaurant getRestaurantByName(@RequestParam String name) {
        return restaurantRepository.findByName(name)
                .orElseThrow(() -> new RestaurantNotFoundException(name));
    }

    @GetMapping(path = "/restaurants", params = "dineInAvailable")
    public List<Restaurant> getRestaurantsByDineInAvailability(@RequestParam boolean dineInAvailable) {
        return restaurantRepository.findByIsDineInAvailable(dineInAvailable)
                .orElseThrow(() -> new RestaurantNotFoundException(dineInAvailable));
    }

    @PostMapping(path = "/restaurants")
    public Restaurant addNewRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    // @TODO: Inefficient since 2 db accesses
    @DeleteMapping(path = "/restaurants/{id}")
    public void removeRestaurantById(@PathVariable Long id) {
        if(restaurantRepository.existsById(id)) {
            restaurantRepository.deleteById(id);
        }
    }

    // @TODO: Inefficient since 2 db accesses. Use proxy objects or SQL.
    @PatchMapping(path = "/restaurants/{id}")
    public Restaurant updateRestaurantSeats(@PathVariable Long id, @RequestParam int numberOfSeats) {
        Restaurant restaurant = getRestaurantByIdOrThrow(id);
        restaurant.setNumberOfSeats(numberOfSeats);
        return restaurantRepository.save(restaurant);
    }

    @PutMapping(path = "/restaurants/{id}")
    public Restaurant updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurant) {
        restaurant.setId(id);
        return restaurantRepository.save(restaurant);
    }

    private Restaurant getRestaurantByIdOrThrow(Long id) throws RestaurantNotFoundException {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
    }
}
