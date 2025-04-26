package com.crombucket.bucketservice.repository;

import org.springframework.stereotype.Repository;

import com.crombucket.bucketservice.enity.Buckets;

import reactor.core.publisher.Flux;


@Repository
public interface BucketsRepository  {

    Flux<Buckets> findAllByCLientId(String clientId, Integer pageNumber ,Integer pageSize);
}
