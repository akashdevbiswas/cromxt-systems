package com.crombucket.storagemanager.dtos.response;

public record BucketResponse(
    String bucketName,
    String bucketCode,
    Long availableSpace
    ) {
}
