package com.cromxt.routeservice.service;


import com.cromxt.common.crombucket.routeing.BucketDetails;
import reactor.core.publisher.Flux;

public interface RouteService {
    Flux<BucketDetails> getAllAvailableRoutes();
}
