package com.zomato.restaurantapi.repository;

import com.zomato.restaurantapi.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByName(String name);
    Optional<List<Restaurant>> findByIsDineInAvailable(boolean dineInAvailable);

    @Modifying
    @Transactional
    @Query("update Restaurant r set r.numberOfSeats = :numberOfSeats where r.id = :id")
    int updateRestaurantSeats(@Param("id") Long id, @Param("numberOfSeats") int numberOfSeats);
}
