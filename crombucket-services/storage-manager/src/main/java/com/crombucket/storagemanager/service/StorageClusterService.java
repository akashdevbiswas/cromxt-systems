package com.crombucket.storagemanager.service;

import com.crombucket.storagemanager.dtos.requests.ClusterRequest;
import com.crombucket.storagemanager.dtos.response.ClusterResponse;
import com.crombucket.storagemanager.repository.Page;
import com.crombucket.storagemanager.utility.SortingOrder;
import reactor.core.publisher.Mono;

public interface StorageClusterService {
    Mono<ClusterResponse> createNewCluster(String regionCode, ClusterRequest clusterRequest);

    Mono<Page<ClusterResponse>> getAllClusters(Integer pageNumber, Integer pageSize, SortingOrder order, String regionCodeOrName);

    Mono<ClusterResponse> getClusterById(String clusterCode);

    Mono<Long> deleteCluster(String clusterCode);
}
