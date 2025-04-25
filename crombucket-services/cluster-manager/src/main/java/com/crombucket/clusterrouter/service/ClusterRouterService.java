package com.crombucket.clusterrouter.service;

import com.crombucket.common.routeing.StorageServerAddress;
import com.crombucket.common.routeing.MediaDetails;
import reactor.core.publisher.Mono;

public interface ClusterRouterService {
    Mono<StorageServerAddress> getBucketDetails(MediaDetails mediaDetails);
}
