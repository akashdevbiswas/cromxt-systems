package com.cromxt.clusterrouter.service;

import com.cromxt.userservice.routeing.StorageServerAddress;
import com.cromxt.userservice.routeing.MediaDetails;
import reactor.core.publisher.Mono;

public interface ClusterRouterService {
    Mono<StorageServerAddress> getBucketDetails(MediaDetails mediaDetails);
}
