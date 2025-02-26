package com.cromxt.routeservice.service;


import com.cromxt.routeservice.dtos.BucketInformationDTO;
import reactor.core.publisher.Flux;

public interface RoutingAdminServices {
    Flux<BucketInformationDTO> getAllOnlineBuckets();
}
