package com.zomato.restaurantapi.repository;

import com.zomato.restaurantapi.model.Restaurant;
import com.zomato.restaurantapi.repository.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RestaurantRedisRepository {
    @Autowired
    private RedisTemplate<String, Restaurant> redisTemplate;

    private ValueOperations<String, Restaurant> getValueOperations() {
        return redisTemplate.opsForValue();
    }

    public Restaurant get(Long id) {
        return getValueOperations().get(RedisUtils.getKey(id));
    }

    public void put(Restaurant restaurant) {
        getValueOperations().set(RedisUtils.getKey(restaurant.getId()), restaurant);
    }

    public Boolean evict(Long id) {
        return redisTemplate.delete(RedisUtils.getKey(id));
    }

    public Boolean hasRestaurant(Long id) {
        return redisTemplate.hasKey(RedisUtils.getKey(id));
    }
}
