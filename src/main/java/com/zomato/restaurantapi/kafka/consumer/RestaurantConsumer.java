package com.zomato.restaurantapi.kafka.consumer;

import com.zomato.restaurantapi.kafka.utils.KafkaUtils;
import com.zomato.restaurantapi.model.Restaurant;
import com.zomato.restaurantapi.kafka.service.RestaurantConsumerServiceImpl;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RestaurantConsumer {
    private static final String TOPIC = KafkaUtils.TOPICS.RESTAURANT_CREATE_TOPIC;

    private static final Logger log = LoggerFactory.getLogger(RestaurantConsumer.class);

    @Autowired
    private RestaurantConsumerServiceImpl restaurantConsumerService;

    @KafkaListener(topics = TOPIC)
    public void consume(ConsumerRecord<String, Restaurant> consumerRecord) {
        log.info("Consuming " + consumerRecord.value()
                + " from partition " + consumerRecord.partition()
                + " having offset " + consumerRecord.offset());
        restaurantConsumerService.handleAddNewRestaurant(consumerRecord.value());
    }
}
