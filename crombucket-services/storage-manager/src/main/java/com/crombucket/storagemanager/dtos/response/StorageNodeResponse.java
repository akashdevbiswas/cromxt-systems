package com.crombucket.storagemanager.dtos.response;

public record StorageNodeResponse(
        String nodeId,
        Long availableSpace
) {
}
