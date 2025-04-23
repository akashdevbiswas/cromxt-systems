package com.cromxt.clusterrouter.controller;

import com.cromxt.clusterrouter.service.ClusterRouterService;
import com.cromxt.jwt.routeing.StorageServerAddress;
import com.cromxt.jwt.routeing.MediaDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/routes")
public record RouterController(
        ClusterRouterService clusterRouterService
        ) {
    @PostMapping
    public Mono<ResponseEntity<StorageServerAddress>> getBucketId(
            @RequestBody MediaDetails mediaDetails) {
        return clusterRouterService.getBucketDetails(mediaDetails).map(bucketDetails -> new ResponseEntity<>(bucketDetails, HttpStatus.OK))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(new StorageServerAddress(), HttpStatus.BAD_REQUEST)));
    }
}
