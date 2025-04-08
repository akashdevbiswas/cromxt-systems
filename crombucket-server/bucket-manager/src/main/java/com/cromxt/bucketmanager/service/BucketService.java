package com.cromxt.bucketmanager.service;

import com.cromxt.bucketmanager.dtos.request.BucketRequest;
import com.cromxt.bucketmanager.dtos.response.BucketResponse;
import reactor.core.publisher.Mono;

public interface BucketService {
    Mono<BucketResponse> addBucket(String clientId, BucketRequest bucketRequest);

    Mono<BucketResponse> updateBucket(String bucketId, BucketRequest bucketRequest);

    Mono<BucketResponse> removeBucket(String bucketId);

    Mono<StorageServerAddress> fetchStorageServerAddress(String clientId, MediaDetails mediaDetails);

}
