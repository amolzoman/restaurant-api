package com.zomato.restaurantapi.kafka.consumer;

import com.zomato.restaurantapi.kafka.utils.KafkaUtils;
import com.zomato.restaurantapi.model.Restaurant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DatabaseConsumer {
    private static final String TOPIC = KafkaUtils.TOPICS.RESTAURANT_CREATE_TOPIC;

    private static final Logger log = LoggerFactory.getLogger(DatabaseConsumer.class);

    @KafkaListener(topics = TOPIC)
    public void consume(ConsumerRecord<String, Restaurant> consumerRecord) {
        log.info("Consuming " + consumerRecord.value());
    }
}
