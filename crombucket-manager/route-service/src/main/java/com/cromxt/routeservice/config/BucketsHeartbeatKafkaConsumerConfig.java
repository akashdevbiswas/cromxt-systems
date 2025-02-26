package com.cromxt.routeservice.config;

import com.cromxt.common.crombucket.kafka.BucketHeartBeat;
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
public class BucketsHeartbeatKafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, BucketHeartBeat> bucketsHeartbeatConsumerFactory(Environment environment) {
        String bootstrapServers = environment.getProperty("ROUTE_SERVICE_CONFIG_HEARTBEAT_BOOTSTRAP_SERVERS", String.class);
        String heartbeatGroupId = environment.getProperty("ROUTE_SERVICE_CONFIG_HEARTBEAT_GROUP_ID", String.class);

        assert bootstrapServers!=null && heartbeatGroupId!=null;

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, heartbeatGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "com.cromxt.common.crombucket.kafka.BucketHeartBeat");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.cromxt.common.crombucket.kafka.BucketHeartBeat"); // Ensure correct type
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BucketHeartBeat> bucketsHeartbeatKafkaListenerContainerFactory(Environment environment) {
        ConcurrentKafkaListenerContainerFactory<String, BucketHeartBeat> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bucketsHeartbeatConsumerFactory(environment));
        return factory;
    }
}
