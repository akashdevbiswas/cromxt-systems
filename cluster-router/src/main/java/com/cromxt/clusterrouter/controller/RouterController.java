package com.cromxt.clusterrouter.controller;

import com.cromxt.common.crombucket.routeing.BucketDetailsResponse;
import com.cromxt.common.crombucket.routeing.MediaDetails;
import com.cromxt.clusterrouter.service.RoutingManagementService;
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
        RoutingManagementService clusterService
        ) {
    @PostMapping("/get-bucket-id")
    public Mono<ResponseEntity<BucketDetailsResponse>> getBucketId(
            @RequestBody MediaDetails mediaDetails) {
        return clusterService.getBucketDetails(mediaDetails).map(bucketDetails -> new ResponseEntity<>(bucketDetails, HttpStatus.OK))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(new BucketDetailsResponse(), HttpStatus.BAD_REQUEST)));
    }
}
