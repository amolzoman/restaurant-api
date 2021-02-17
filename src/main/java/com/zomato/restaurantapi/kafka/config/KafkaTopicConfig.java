package com.zomato.restaurantapi.kafka.config;

import com.zomato.restaurantapi.kafka.utils.KafkaUtils;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    @Value("${kafka.bootstrapServer}")
    private String bootstrapServer;

    @Value("${kafka.topic.partitions}")
    private int numPartitions;

    @Value("${kafka.topic.replication-factor}")
    private short replicationFactor;

    @Bean
    public Map<String, Object> kafkaAdminConfig() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        return configs;
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return new KafkaAdmin(kafkaAdminConfig());
    }

    @Bean
    public NewTopic newRestaurantTopic() {
        return new NewTopic(KafkaUtils.TOPICS.RESTAURANT_CREATE_TOPIC, numPartitions, replicationFactor);
    }
}
