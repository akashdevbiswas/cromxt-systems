package com.cromxt.clusterrouter.service;

import com.cromxt.jwt.routeing.StorageServerAddress;
import com.cromxt.jwt.routeing.MediaDetails;
import reactor.core.publisher.Mono;

public interface ClusterRouterService {
    Mono<StorageServerAddress> getBucketDetails(MediaDetails mediaDetails);
}
