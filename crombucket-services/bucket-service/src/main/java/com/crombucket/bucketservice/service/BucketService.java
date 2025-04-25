package com.crombucket.bucketservice.service;

import com.crombucket.bucketservice.dtos.request.BucketRequest;
import com.crombucket.bucketservice.dtos.response.BucketResponse;
import reactor.core.publisher.Mono;

public interface BucketService {

    Mono<BucketResponse> saveBucket(BucketRequest bucketRequest);
}
