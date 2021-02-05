package com.zomato.restaurantapi.controller;

import com.zomato.restaurantapi.model.Restaurant;
import com.zomato.restaurantapi.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @GetMapping(path = "/restaurants")
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.findAll();
    }

    @GetMapping(path = "/restaurants/{id}")
    public Restaurant getRestaurantById(@PathVariable Long id) {
        return restaurantService.findById(id);
    }

    @GetMapping(path = "/restaurants", params = "name")
    public Restaurant getRestaurantByName(@RequestParam String name) {
        return restaurantService.findByName(name);
    }

    @GetMapping(path = "/restaurants", params = "dineInAvailable")
    public List<Restaurant> getRestaurantsByDineInAvailability(@RequestParam boolean dineInAvailable) {
        return restaurantService.findByDineInAvailability(dineInAvailable);
    }

    @PostMapping(path = "/restaurants")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addNewRestaurant(@RequestBody Restaurant restaurant) {
        restaurantService.save(restaurant);
    }

    @DeleteMapping(path = "/restaurants/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRestaurantById(@PathVariable Long id) {
        restaurantService.removeById(id);
    }

    @PatchMapping(path = "/restaurants/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRestaurantSeats(@PathVariable Long id, @RequestParam int numberOfSeats) {
        restaurantService.updateSeats(id, numberOfSeats);
    }

    @PutMapping(path = "/restaurants/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurant) {
        restaurantService.update(id, restaurant);
    }
}
