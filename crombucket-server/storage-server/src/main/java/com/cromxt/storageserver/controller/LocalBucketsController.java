package com.cromxt.storageserver.controller;


import com.cromxt.storageserver.service.impl.BucketInformationService;
import com.cromxt.common.crombucket.routeing.StorageServerAddress;
import com.cromxt.common.crombucket.routeing.MediaDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bucket-manager/api/v1/buckets")
@Profile({"local","local-docker","local-docker-dev"})
@RequiredArgsConstructor
public class LocalBucketsController {

    private final BucketInformationService bucketInformationService;

    @PostMapping(value = "/fetch-storage-address")
    public StorageServerAddress getBucketDetails(@RequestBody MediaDetails ignored) {
        return StorageServerAddress.builder()
                .hostName(bucketInformationService.getRouteIp())
                .rpcPort(bucketInformationService.getRpcPort())
                .build();
    }
}
