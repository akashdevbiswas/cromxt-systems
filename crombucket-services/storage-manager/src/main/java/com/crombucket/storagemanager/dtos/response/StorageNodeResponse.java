package com.crombucket.storagemanager.dtos.response;

import java.time.LocalDate;

public record StorageNodeResponse(
        String id,
        String storageCode,
        Long availableSpace,
        LocalDate createdAt,
        LocalDate joinedCluster
) {
}
