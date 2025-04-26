package com.crombucket.bucketservice.service.impl;

import com.crombucket.bucketservice.dtos.request.BucketRequest;
import com.crombucket.bucketservice.dtos.response.BucketResponse;
import com.crombucket.bucketservice.dtos.response.BucketsListResponse;
import com.crombucket.bucketservice.enity.Buckets;
import com.crombucket.bucketservice.repository.BucketsRepository;
import com.crombucket.bucketservice.service.BucketService;
import com.crombucket.bucketservice.service.EntityMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class BucketServiceImpl implements BucketService {

    private final BucketsRepository bucketsRepository;
    private final EntityMapper entityMapper;

    @Override
    public Mono<BucketResponse> saveBucket(BucketRequest bucketRequest) {
        return Mono.just(BucketResponse.builder()
                .bucketId("Long-bucket-id")
                .build());
    }

    @Override
    public Mono<BucketsListResponse> getAllBucketsByUserId(String userId, Integer pageNumber, Integer pageSize) {
        Flux<Buckets> buckets = bucketsRepository.findAllByCLientId(userId, pageNumber,pageSize);
        buckets
        .collectList()
        .doOnNext((bucket)->{
            System.out.println(bucket);
        }).subscribe();
        return Mono.empty();
    }
}
