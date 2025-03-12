package com.cromxt.storageserver.controller;


import com.cromxt.storageserver.service.impl.BucketInformationService;
import com.cromxt.common.crombucket.routeing.StorageServerAddress;
import com.cromxt.common.crombucket.routeing.MediaDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/bucket-manager/api/v1/buckets")
@Profile("local")
public class LocalBucketsController {

    private final BucketInformationService bucketInformationService;
    private final String hostname;

    public LocalBucketsController(BucketInformationService bucketInformationService, Environment environment) {
        this.bucketInformationService = bucketInformationService;

        String profile = environment.getProperty("spring.profiles.active",String.class);
        if(Objects.equals(profile, "docker")){
            hostname = bucketInformationService.getApplicationHostname();
        }else {
            this.hostname = "localhost";
        }
    }

    @PostMapping(value = "/fetch-storage-address")
    public StorageServerAddress getBucketDetails(@RequestBody MediaDetails ignored) {
        return StorageServerAddress.builder()
                .hostName(hostname)
                .rpcPort(bucketInformationService.getRpcPort())
                .build();
    }
}
