package com.cromxt.bucket.controller;


import com.cromxt.bucket.service.impl.BucketInformationService;
import com.cromxt.bucket.service.impl.LocalMediaHandlerGRPCServiceImpl;
import com.cromxt.common.crombucket.routeing.BucketDetails;
import com.cromxt.common.crombucket.routeing.MediaDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/buckets")
@RequiredArgsConstructor
@Profile("local")
public class BucketServiceController {

    private final BucketInformationService bucketInformationService;
    private final LocalMediaHandlerGRPCServiceImpl mediaHandlerGRPCService;

    @PostMapping
    public BucketDetails getBucketDetails(@RequestBody MediaDetails ignored) {
        return BucketDetails.builder()
                .bucketId(bucketInformationService.getBucketId())
                .hostName(bucketInformationService.getApplicationHostname())
                .httpPort(bucketInformationService.getHttpPort())
                .rpcPort(bucketInformationService.getRpcPort())
                .build();
    }

    public Flux<MediaDetails> getAllAvailableMedias(){
        return Flux.empty();
    }
}
