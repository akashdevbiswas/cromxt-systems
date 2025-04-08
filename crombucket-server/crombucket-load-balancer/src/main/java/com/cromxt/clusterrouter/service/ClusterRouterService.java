package com.cromxt.clusterrouter.service;

import com.cromxt.crombucket.routeing.StorageServerAddress;
import com.cromxt.crombucket.routeing.MediaDetails;
import reactor.core.publisher.Mono;

public interface ClusterRouterService {
    Mono<StorageServerAddress> getBucketDetails(MediaDetails mediaDetails);
}
