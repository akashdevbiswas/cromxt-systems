package com.crombucket.clusterrouter.utils;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class RoutingProperties {

    private String clusterId;
    private String heartBeatTopic;
    private String heartBeatBootstrapServers;
    private Integer loadFactor;
    private String bucketManagerAddress;
}
