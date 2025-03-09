package com.cromxt.bucket.controller;


import com.cromxt.bucket.service.impl.BucketInformationService;
import com.cromxt.common.crombucket.routeing.BucketDetailsResponse;
import com.cromxt.common.crombucket.routeing.MediaDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/routes")
@RequiredArgsConstructor
@Profile("local")
public class RoutingController {

    private final BucketInformationService bucketInformationService;

    @PostMapping
    public BucketDetailsResponse getBucketDetails(@RequestBody MediaDetails ignored) {
        return BucketDetailsResponse.builder()
                .hostName(bucketInformationService.getApplicationHostname())
                .rpcPort(bucketInformationService.getRpcPort())
                .build();
    }
}
