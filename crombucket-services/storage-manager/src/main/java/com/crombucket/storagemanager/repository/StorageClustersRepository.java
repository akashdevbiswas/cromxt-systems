package com.crombucket.storagemanager.repository;

import com.crombucket.storagemanager.entity.StorageClusters;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StorageClustersRepository {

    Mono<StorageClusters> saveClusters(StorageClusters cluster);

    Flux<StorageClusters> findAllClusters(Pageable pageable);

    Mono<StorageClusters> findClusterById(String clusterId);

    Mono<Long> deleteCluster(String clusterId);


}
