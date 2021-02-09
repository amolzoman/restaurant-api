package com.zomato.restaurantapi.service;

import com.zomato.restaurantapi.exception.RestaurantNotFoundException;
import com.zomato.restaurantapi.kafka.service.RestaurantProducerServiceImpl;
import com.zomato.restaurantapi.model.Restaurant;
import com.zomato.restaurantapi.repository.RestaurantRedisRepository;
import com.zomato.restaurantapi.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {
    @Autowired
    private RestaurantProducerServiceImpl restaurantProducerService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantRedisRepository restaurantRedisRepository;

    private Restaurant findByIdOrThrow(Long id) throws RestaurantNotFoundException {
        if(restaurantRedisRepository.hasRestaurant(id)) {
            return restaurantRedisRepository.get(id);
        }

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
        restaurantRedisRepository.put(restaurant);

        return restaurant;
    }

    private Restaurant findByNameOrThrow(String name) throws RestaurantNotFoundException {
        return restaurantRepository.findByName(name)
                .orElseThrow(() -> new RestaurantNotFoundException(name));
    }

    private List<Restaurant> findByDineInAvailabilityOrThrow(boolean dineInAvailable) throws RestaurantNotFoundException {
        return restaurantRepository.findByIsDineInAvailable(dineInAvailable)
                .orElseThrow(() -> new RestaurantNotFoundException(dineInAvailable));
    }

    @Override
    public List<Restaurant> findAll() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        for(Restaurant restaurant : restaurants) {
            restaurantRedisRepository.put(restaurant);
        }

        return restaurants;
    }

    @Override
    public void save(Restaurant restaurant) {
        restaurantProducerService.handleAddNewRestaurant(restaurant);
    }

    @Override
    public Restaurant findById(Long id) {
        return findByIdOrThrow(id);
    }

    @Override
    public Restaurant findByName(String name) {
        return findByNameOrThrow(name);
    }

    @Override
    public List<Restaurant> findByDineInAvailability(boolean dineInAvailable) {
        return findByDineInAvailabilityOrThrow(dineInAvailable);
    }

    /*
     * @TODO: Inefficient since 2 db accesses
     * existsById() might be using a count query
     * No docs mention this but this is what I think after digging through the source code,
     * but, if I were you I won't take my word.
     * someone write proper docs!
     */
    @Override
    public void removeById(Long id) {
        if(!restaurantRepository.existsById(id)) {
            throw new RestaurantNotFoundException(id);
        }

        restaurantRedisRepository.evict(id);
        restaurantRepository.deleteById(id);
    }

    @Override
    public void updateSeats(Long id, int numberOfSeats) {
        restaurantRedisRepository.evict(id);
        int changedRecords = restaurantRepository.updateRestaurantSeats(id, numberOfSeats);
        if(changedRecords == 0) {
            throw new RestaurantNotFoundException(id);
        }
    }

    @Override
    public void update(Long id, Restaurant restaurant) {
        restaurantRedisRepository.evict(id);
        restaurant.setId(id);
        save(restaurant);
    }
}
