package com.crombucket.bucketservice.service;

import com.crombucket.bucketservice.dtos.request.StorageRequest;
import com.crombucket.bucketservice.dtos.request.BucketRequest;
import com.crombucket.bucketservice.enity.BucketGroups;
import com.crombucket.bucketservice.enity.Buckets;

public interface EntityMapper {

    Buckets generateBucketFromBucketRequest(BucketRequest bucketRequest);

    BucketGroups generateBucketsGroupFromBucketGroupRequest(StorageRequest bucketGroupRequest);


}
