package com.cromxt.clusterrouter.service;


import com.cromxt.clusterrouter.dtos.BucketInformationDTO;
import reactor.core.publisher.Flux;

public interface RoutingAdminServices {
    Flux<BucketInformationDTO> getAllOnlineBuckets();
}
