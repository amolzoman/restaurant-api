package com.zomato.restaurantapi.repository.utils;

public class RedisUtils {
    private static String getKey(String prefix, Object suffix) {
        if(suffix == null) {
            return prefix;
        }
        return prefix + ":" + suffix.toString();
    }

    public static String getKey(Long id) {
        return getKey(KEY_PREFIX.RESTAURANT_ID, id);
    }

    public static String getKey(String name) {
        return getKey(KEY_PREFIX.RESTAURANT_NAME, name);
    }

    public static String getKeyAll() {
        return getKey(KEY_PREFIX.ALL, null);
    }

    public static String getKeyDineInAvailability(boolean dineInAvailability) {
        return getKey(KEY_PREFIX.DINE_IN, dineInAvailability);
    }

    public interface KEY_PREFIX {
        String ALL = "all";
        String RESTAURANT_ID = "restaurant";
        String RESTAURANT_NAME = "restaurant-name";
        String DINE_IN = "dine-in";
    }
}
