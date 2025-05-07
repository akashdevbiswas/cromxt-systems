package com.crombucket.storagemanager.service;

import com.crombucket.storagemanager.dtos.requests.BucketRequest;
import com.crombucket.storagemanager.dtos.response.BucketResponse;

import reactor.core.publisher.Mono;

public interface BucketService {

  Mono<BucketResponse> createBucket(BucketRequest bucketRequest);

}
