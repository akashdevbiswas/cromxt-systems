package com.cromxt.bucket.controller;


import com.cromxt.bucket.service.impl.BucketInformationService;
import com.cromxt.common.crombucket.routeing.BucketDetails;
import com.cromxt.common.crombucket.routeing.MediaDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/buckets")
@RequiredArgsConstructor
public class BucketServiceController {


    private final BucketInformationService bucketInformationService;

    @PostMapping
    public BucketDetails getBucketDetails(@RequestBody MediaDetails ignored) {
        return BucketDetails.builder()
                .bucketId(bucketInformationService.getBucketId())
                .hostName(bucketInformationService.getApplicationHostname())
                .httpPort(bucketInformationService.getHttpPort())
                .rpcPort(bucketInformationService.getRpcPort())
                .build();
    }
}
