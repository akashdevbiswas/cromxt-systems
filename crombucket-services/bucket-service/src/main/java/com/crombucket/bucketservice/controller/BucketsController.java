package com.crombucket.bucketservice.controller;

import com.crombucket.bucketservice.dtos.request.BucketRequest;
import com.crombucket.bucketservice.dtos.response.BucketResponse;
import com.crombucket.bucketservice.dtos.response.BucketTypesResponse;
import com.crombucket.bucketservice.dtos.response.BucketsListResponse;
import com.crombucket.bucketservice.enity.BucketType;
import com.crombucket.bucketservice.service.BucketService;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/buckets")
@RequiredArgsConstructor
public class BucketsController {

    private final BucketService bucketService;

    @PostMapping
    public Mono<ResponseEntity<BucketResponse>> createBucket(@RequestBody BucketRequest bucketRequest,
            Authentication authentication) {
        Mono<BucketResponse> savedBucketResponse = bucketService.saveBucket(bucketRequest);
        return savedBucketResponse
                .map(bucketResponse -> new ResponseEntity<>(bucketResponse, HttpStatus.CREATED))
                .onErrorResume(err -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("X-Message", err.getMessage());
                    return Mono.just(ResponseEntity.badRequest().headers(headers).build());
                });
    }

    @GetMapping(value = "/types")
    @ResponseStatus(code = HttpStatus.OK)
    public Flux<BucketTypesResponse> getAllBucketTypes() {
        List<BucketTypesResponse> bucketTypes = Arrays.stream(BucketType.values())
                .map(bucketType -> new BucketTypesResponse(bucketType.name(), bucketType.getBucketSize())).toList();
        return Flux.fromIterable(bucketTypes);
    }

    @GetMapping
    public Mono<ResponseEntity<BucketsListResponse>> getAllBuckets(
        @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
        @RequestParam(required = false ,defaultValue = "20")Integer pageSize ,
        Authentication authentication){
        String userId = (String) authentication.getPrincipal();
        return bucketService.getAllBucketsByUserId(userId,pageNumber,pageSize)
        .map(bucketList->{
            return ResponseEntity.ok(bucketList);
        });
    }

}
