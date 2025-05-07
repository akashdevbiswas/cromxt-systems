package com.crombucket.storagemanager.repository;

import org.springframework.data.domain.Pageable;

import com.crombucket.storagemanager.entity.Clusters;

import reactor.core.publisher.Mono;

public interface ClustersRepository {

    Mono<Clusters> saveClusters(Clusters cluster);

    Mono<Page<Clusters>> findAllClusters(Pageable pageable);

    Mono<Clusters> findClusterByClusterCode(String clusterCode);

    Mono<Long> deleteCluster(String clusterCode);

    Mono<Clusters> getLargeCluster();

}
