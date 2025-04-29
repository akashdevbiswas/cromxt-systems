package com.crombucket.storagemanager.service;

import com.crombucket.storagemanager.dtos.requests.ClusterRequest;
import com.crombucket.storagemanager.dtos.response.ClusterResponse;
import com.crombucket.storagemanager.repository.Page;
import com.crombucket.storagemanager.utility.ClusterSortingOrder;
import reactor.core.publisher.Mono;

public interface StorageClusterService {
    Mono<ClusterResponse> createNewCluster(ClusterRequest clusterRequest);

    Mono<Page<ClusterResponse>> getAllStorageClusters(Integer pageNumber, Integer pageSize, ClusterSortingOrder order);

    Mono<ClusterResponse> getClusterById(String clusterCode);

    Mono<Long> deleteCluster(String clusterCode);
}
