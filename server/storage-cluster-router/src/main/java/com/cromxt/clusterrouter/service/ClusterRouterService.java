package com.cromxt.clusterrouter.service;

import com.cromxt.common.crombucket.routeing.BucketDetailsResponse;
import com.cromxt.common.crombucket.routeing.MediaDetails;
import reactor.core.publisher.Mono;

public interface ClusterRouterService {
    Mono<BucketDetailsResponse> getBucketDetails(MediaDetails mediaDetails);
}
