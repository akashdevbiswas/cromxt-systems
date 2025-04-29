package com.crombucket.storagemanager.dtos.requests;

public record StorageNodeRequest(
        String storageNodeCode,
        String clusterCode
) {
}
