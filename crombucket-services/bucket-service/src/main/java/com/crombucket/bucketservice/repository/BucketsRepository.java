package com.crombucket.bucketservice.repository;

import com.crombucket.bucketservice.enity.Buckets;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BucketsRepository extends ReactiveMongoRepository<Buckets,String> {
}
