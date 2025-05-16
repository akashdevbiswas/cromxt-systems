package com.crombucket.storagemanager.repository;


import org.springframework.data.domain.Pageable;

import com.crombucket.storagemanager.entity.Regions;

import reactor.core.publisher.Mono;

public interface RegionRepository {
  Mono<Regions> saveRegion(Regions region);

  Mono<Regions> findRegionByRegionCode(String regionCode);

  Mono<Page<Regions>> findRegions(String regionNameOrCode, Pageable pageable);
}
