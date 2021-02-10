package com.zomato.restaurantapi.repository;

import com.zomato.restaurantapi.model.Restaurant;
import com.zomato.restaurantapi.repository.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Repository
public class RestaurantRedisRepositoryImpl implements RestaurantRedisRepository {
    @Autowired
    private RedisTemplate<String, Restaurant> redisTemplate;

    private final long expireAfterSeconds = 60;

    private ValueOperations<String, Restaurant> getValueOperations() {
        return redisTemplate.opsForValue();
    }

    private HashOperations<String, Long, Restaurant> getHashOperations() {
        return redisTemplate.opsForHash();
    }

    @Override
    public Restaurant get(Long id) {
        return getValueOperations().get(RedisUtils.getKey(id));
    }

    @Override
    public void put(Restaurant restaurant) {
        getValueOperations().set(RedisUtils.getKey(restaurant.getId()), restaurant);
    }

    @Override
    public Boolean evict(Long id) {
        return redisTemplate.delete(RedisUtils.getKey(id));
    }

    @Override
    public Boolean hasRestaurant(Long id) {
        return redisTemplate.hasKey(RedisUtils.getKey(id));
    }

    @Override
    public List<Restaurant> getAll() {
        return getAll(RedisUtils.getKeyAll());
    }

    @Override
    public List<Restaurant> getAllDineIn(boolean dineInAvailable) {
        return getAll(RedisUtils.getKeyDineInAvailability(dineInAvailable));
    }

    @Override
    public void putAll(List<Restaurant> restaurants) {
        this.put(restaurants);
        this.put(RedisUtils.getKeyAll(), restaurants);
    }

    @Override
    public void putAllDineIn(List<Restaurant> restaurants, boolean dineInAvailable) {
        this.put(restaurants);
        this.put(RedisUtils.getKeyDineInAvailability(dineInAvailable), restaurants);
    }


    private void put(List<Restaurant> restaurants) {
        for(Restaurant restaurant : restaurants) {
            this.put(restaurant);
        }
    }

    private void put(String KEY, List<Restaurant> restaurants) {
        getHashOperations().putAll(KEY, restaurants
                .stream()
                .collect(Collectors.toMap(Restaurant::getId, restaurant -> restaurant)));
        redisTemplate.expire(KEY, expireAfterSeconds, TimeUnit.SECONDS);
    }

    private List<Restaurant> getAll(String Key) {
        Boolean hasKey = redisTemplate.hasKey(Key);

        if(hasKey == null || !hasKey) {
            return null;
        }

        return getHashOperations().values(Key);
    }
}
