package com.crombucket.clusterrouter.config;


import com.crombucket.clusterrouter.utils.RoutingProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApplicationConfig {


    @Bean
    WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public RoutingProperties applicationProperties(Environment environment) {
        String clusterId = environment.getProperty("ROUTE_SERVICE_CONFIG_CLUSTER_ID", String.class);
        String heartbeatTopic = environment.getProperty("ROUTE_SERVICE_CONFIG_BUCKET_HEARTBEAT_TOPIC", String.class);
        String kafkaBootstrapServers = environment.getProperty("ROUTE_SERVICE_CONFIG_HEARTBEAT_BOOTSTRAP_SERVERS", String.class);
        String bucketMangerClientAddress = environment.getProperty("ROUTE_SERVICE_CONFIG_BUCKET_MANAGER_CLIENT_ADDRESS", String.class);
        Integer loadFactor = environment.getProperty("ROUTE_SERVICE_CONFIG_BUCKET_LOAD_FACTOR", Integer.class);

        assert clusterId != null && heartbeatTopic != null && kafkaBootstrapServers != null && bucketMangerClientAddress != null && loadFactor != null;

        return RoutingProperties.builder()
                .clusterId(clusterId)
                .heartBeatTopic(heartbeatTopic)
                .bucketManagerAddress(bucketMangerClientAddress)
                .loadFactor(loadFactor)
                .heartBeatBootstrapServers(kafkaBootstrapServers)
                .build();
    }
}
