package com.crombucket.storagemanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crombucket.storagemanager.dtos.response.BucketCredentials;
import com.crombucket.storagemanager.dtos.response.BucketResponse;
import com.crombucket.storagemanager.service.BucketService;
import com.crombucket.common.ResponseBuilder;
import com.crombucket.storagemanager.dtos.requests.BucketRequest;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequestMapping(value = "/api/v1/buckets")
@RequiredArgsConstructor
@RestController
public class BucketsController {

  private final BucketService bucketService;
  private final ResponseBuilder responseBuilder;

  @PostMapping
  public Mono<ResponseEntity<BucketResponse>> createBucket(@RequestBody BucketRequest bucketRequest) {
    Mono<BucketResponse> bucketMono = bucketService.createBucket(bucketRequest);
    return responseBuilder.buildResponseWithBody(bucketMono, HttpStatus.CREATED);
  }

  @GetMapping(value = "/access-key/{bucketId}")
  public Mono<ResponseEntity<BucketCredentials>> getCredentials(@PathVariable String bucketId){
    return null;
  }

}
