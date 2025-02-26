package com.cromxt.routeservice.service;


import com.cromxt.common.crombucket.routeing.BucketDetails;
import com.cromxt.common.crombucket.routeing.MediaDetails;
import reactor.core.publisher.Mono;

public interface RoutingService {

    Mono<BucketDetails> getBucketDetails(MediaDetails mediaDetails);
}
