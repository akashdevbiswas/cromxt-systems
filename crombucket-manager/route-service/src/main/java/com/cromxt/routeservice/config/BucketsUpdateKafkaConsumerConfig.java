package com.cromxt.routeservice.config;

import com.cromxt.common.crombucket.kafka.BucketUpdateRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class BucketsUpdateKafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, BucketUpdateRequest> bucketsUpdateConsumerFactory(Environment environment) {
        String bootstrapServers = environment.getProperty("ROUTE_SERVICE_CONFIG_BUCKET_UPDATE_BOOTSTRAP_SERVERS", String.class);
        String bucketUpdateGroupId = environment.getProperty("ROUTE_SERVICE_CONFIG_BUCKET_UPDATE_GROUP_ID", String.class);

        assert bootstrapServers != null && bucketUpdateGroupId != null;

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, bucketUpdateGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.cromxt.common.crombucket.kafka.BucketUpdateRequest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.cromxt.common.crombucket.kafka.BucketUpdateRequest"); // Ensure correct type
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BucketUpdateRequest> bucketsUpdateKafkaListenerContainerFactory(Environment environment) {
        ConcurrentKafkaListenerContainerFactory<String, BucketUpdateRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bucketsUpdateConsumerFactory(environment));
        return factory;
    }
}
