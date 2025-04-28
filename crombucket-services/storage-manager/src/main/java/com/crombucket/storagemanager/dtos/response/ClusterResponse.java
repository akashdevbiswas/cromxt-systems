package com.crombucket.storagemanager.dtos.response;

import java.util.List;

public record ClusterResponse(
        String clusterId,
        String clusterCode,
        Long capacity
) {
}
