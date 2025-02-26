package com.cromxt.routeservice.controller;

import com.cromxt.common.crombucket.routeing.BucketDetails;
import com.cromxt.common.crombucket.routeing.MediaDetails;
import com.cromxt.routeservice.service.RoutingService;
import com.cromxt.routeservice.service.impl.AvailableRouteDiscovererService;
import com.cromxt.routeservice.service.impl.BucketManager;
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
        RoutingService routingService
        ) {
    @PostMapping("/get-bucket-id")
    public Mono<ResponseEntity<BucketDetails>> getBucketId(
            @RequestBody MediaDetails mediaDetails) {
        return routingService.getBucketDetails(mediaDetails).map(bucketDetails -> new ResponseEntity<>(bucketDetails, HttpStatus.OK))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(new BucketDetails(), HttpStatus.OK)));
    }
}
