package com.crombucket.common.bucketservice.responses;



public record StorageNodeDetails(
    String storageId,
    String clusterId,
    String url,
    Integer port
) {

} 
