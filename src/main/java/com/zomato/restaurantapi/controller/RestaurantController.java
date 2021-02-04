package com.zomato.restaurantapi.controller;

import com.zomato.restaurantapi.exception.RestaurantNotFoundException;
import com.zomato.restaurantapi.kafka.service.RestaurantProducerServiceImpl;
import com.zomato.restaurantapi.model.Restaurant;
import com.zomato.restaurantapi.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
public class RestaurantController {
    @Autowired
    private RestaurantProducerServiceImpl restaurantProducerService;

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
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addNewRestaurant(@RequestBody Restaurant restaurant) {
        restaurantProducerService.handleAddNewRestaurant(restaurant);
    }

    // @TODO: Inefficient since 2 db accesses
    // existsById() might be using a count query
    // No docs mention this but this is what I think after digging through the source code,
    // but, if I were you I won't take my word.
    // someone write proper docs!
    @DeleteMapping(path = "/restaurants/{id}")
    public ResponseEntity<?> removeRestaurantById(@PathVariable Long id) {
        if(!restaurantRepository.existsById(id)) {
            throw new RestaurantNotFoundException(id);
        }
        restaurantRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // @TODO: Inefficient since 2 db accesses. Use proxy objects or SQL.
    @PatchMapping(path = "/restaurants/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateRestaurantSeats(@PathVariable Long id, @RequestParam int numberOfSeats) {
        Restaurant restaurant = getRestaurantByIdOrThrow(id);
        restaurant.setNumberOfSeats(numberOfSeats);
        restaurantProducerService.handleAddNewRestaurant(restaurant);
    }

    @PutMapping(path = "/restaurants/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurant) {
        restaurant.setId(id);
        restaurantProducerService.handleAddNewRestaurant(restaurant);
    }

    private Restaurant getRestaurantByIdOrThrow(Long id) throws RestaurantNotFoundException {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
    }
}
