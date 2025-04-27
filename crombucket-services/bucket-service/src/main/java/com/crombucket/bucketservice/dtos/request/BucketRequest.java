package com.crombucket.bucketservice.dtos.request;


import com.crombucket.bucketservice.enity.StorageNodeType;

import java.util.List;

public record BucketRequest(
        String bucketName,
        List<StorageNodeType> storageNodeList
){
}
