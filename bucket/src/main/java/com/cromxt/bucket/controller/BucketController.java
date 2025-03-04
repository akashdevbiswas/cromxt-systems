package com.cromxt.bucket.controller;


import com.cromxt.bucket.service.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/buckets")
public class BucketController {

    private final BucketService bucketService;



}
