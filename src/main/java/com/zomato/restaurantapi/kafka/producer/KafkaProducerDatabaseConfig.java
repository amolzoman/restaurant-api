package com.zomato.restaurantapi.kafka.producer;

import com.zomato.restaurantapi.model.Restaurant;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaProducerDatabaseConfig {
    @Bean
    public Map<String, Object> producerConfigsDatabase() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return properties;
    }

    @Bean
    public ProducerFactory<String, Restaurant> producerFactoryDatabase() {
        return new DefaultKafkaProducerFactory<>(producerConfigsDatabase());
    }

    @Bean
    public KafkaTemplate<String, Restaurant> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactoryDatabase());
    }
}
