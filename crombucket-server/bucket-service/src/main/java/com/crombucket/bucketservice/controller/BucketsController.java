package com.crombucket.bucketservice.controller;

import com.crombucket.bucketservice.dtos.request.BucketRequest;
import com.crombucket.bucketservice.dtos.response.BucketResponse;
import com.crombucket.bucketservice.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/buckets")
@RequiredArgsConstructor
public class BucketsController {

    private static final String url = "/api/v1/buckets";

    private final BucketService bucketService;

    @PostMapping
    public Mono<ResponseEntity<BucketResponse>> createBucket(@RequestBody BucketRequest bucketRequest) {
        Mono<BucketResponse> savedBucketResponse = bucketService.saveBucket(bucketRequest);
        return savedBucketResponse
                .map(bucketResponse -> new ResponseEntity<>(bucketResponse,HttpStatus.CREATED))
                .onErrorResume(err->{
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("X-Message", err.getMessage());
                    return Mono.just(ResponseEntity.badRequest().headers(headers).build());
                });
    }

}

