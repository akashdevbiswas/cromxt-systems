package com.cromxt.clusterrouter.service;

import com.cromxt.common.crombucket.routeing.BucketDetailsResponse;
import com.cromxt.common.crombucket.routeing.MediaDetails;
import reactor.core.publisher.Mono;

public interface RouteService {
    Mono<BucketDetailsResponse> getBucketDetails(MediaDetails mediaDetails);
}
