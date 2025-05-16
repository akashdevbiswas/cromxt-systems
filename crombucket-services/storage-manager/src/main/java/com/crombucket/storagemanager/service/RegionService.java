package com.crombucket.storagemanager.service;

import com.crombucket.storagemanager.dtos.requests.RegionRequest;
import com.crombucket.storagemanager.dtos.response.RegionResponse;
import com.crombucket.storagemanager.repository.Page;
import com.crombucket.storagemanager.utility.SortingOrder;

import reactor.core.publisher.Mono;

public interface RegionService {
    Mono<RegionResponse> createRegion(RegionRequest regionRequest);

    Mono<RegionResponse> getRegionByCode(String regionCode);

    Mono<Page<RegionResponse>> findAllRegions(String regionName, Integer pageNumber, Integer pageSize, SortingOrder order); 
}
