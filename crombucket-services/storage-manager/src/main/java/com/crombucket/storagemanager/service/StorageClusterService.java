package com.crombucket.storagemanager.service;

import com.crombucket.storagemanager.dtos.requests.ClusterRequest;
import com.crombucket.storagemanager.dtos.response.ClusterResponse;
import com.crombucket.storagemanager.dtos.response.ClustersListResponse;
import com.crombucket.storagemanager.utility.SortingOrder;
import reactor.core.publisher.Mono;

public interface StorageClusterService {
    Mono<ClusterResponse> createNewCluster(ClusterRequest clusterRequest);

    Mono<ClustersListResponse> getAllStorageClusters(Integer pageNumber, Integer pageSize, SortingOrder order);

    Mono<ClusterResponse> getClusterById(String clusterId);

    Mono<Long> deleteCluster(String clusterId);
}
