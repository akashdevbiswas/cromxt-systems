package com.crombucket.storagemanager.dtos.response;

public record ClusterResponse(
        String clusterId,
        String clusterCode,
        Long capacity
) {
}
