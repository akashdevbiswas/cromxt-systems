package com.crombucket.bucketservice.dtos.request;


import com.crombucket.bucketservice.enity.BucketType;

import java.util.List;

public record BucketRequest(
        BucketType bucketType,
        String bucketName,
        List<String> bucketGroups
){
}
