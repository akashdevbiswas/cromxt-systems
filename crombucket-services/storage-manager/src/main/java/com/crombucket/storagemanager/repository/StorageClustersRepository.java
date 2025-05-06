package com.crombucket.storagemanager.repository;

import com.crombucket.storagemanager.entity.Clusters;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StorageClustersRepository {

    Mono<Clusters> saveClusters(Clusters cluster);

    Mono<Page<Clusters>> findAllClusters(Pageable pageable);

    Mono<Clusters> findClusterByClusterCode(String clusterCode);

    Mono<Long> deleteCluster(String clusterCode);

}
