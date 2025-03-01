package com.cromxt.bucket.controller;


import com.cromxt.bucket.models.MediaObjects;
import com.cromxt.bucket.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/buckets")
public class BucketController {

    private final BucketService bucketService;

//   Define the admin end points which will used to manage the bucket.



}
