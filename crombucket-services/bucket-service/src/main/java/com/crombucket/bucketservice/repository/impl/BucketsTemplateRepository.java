
package com.crombucket.bucketservice.repository.impl;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.crombucket.bucketservice.enity.Buckets;
import com.crombucket.bucketservice.repository.BucketsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
@Slf4j
public class BucketsTemplateRepository implements BucketsRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<Buckets> findAllByCLientId(String clientId, Integer pageNumber, Integer pageSize) {
        Query query = new Query()
        .addCriteria(Criteria.where("clientId").is(clientId))
        .skip(pageNumber * pageSize)
        .limit(pageSize);
        return mongoTemplate.find(query, Buckets.class).onErrorResume(err->{
            log.error("Error occurred while fetch data from database with message: {}", err.getMessage());
            return Flux.error(err);
        });
    }

}