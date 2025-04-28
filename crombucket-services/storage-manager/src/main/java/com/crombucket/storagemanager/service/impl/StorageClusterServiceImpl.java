package com.crombucket.storagemanager.service.impl;

import com.crombucket.storagemanager.dtos.requests.ClusterRequest;
import com.crombucket.storagemanager.dtos.response.ClusterResponse;
import com.crombucket.storagemanager.dtos.response.ClustersListResponse;
import com.crombucket.storagemanager.entity.StorageClusters;
import com.crombucket.storagemanager.repository.StorageClustersRepository;
import com.crombucket.storagemanager.service.EntityMapperService;
import com.crombucket.storagemanager.service.StorageClusterService;
import com.crombucket.storagemanager.utility.SortingOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class StorageClusterServiceImpl implements StorageClusterService {

    private final EntityMapperService entityMapperService;
    private final StorageClustersRepository storageClustersRepository;


    @Override
    public Mono<ClusterResponse> createNewCluster(ClusterRequest clusterRequest) {
        StorageClusters storageCluster = entityMapperService.createClusterEntityFromClusterRequest(clusterRequest);
        return storageClustersRepository.saveClusters(storageCluster).map(entityMapperService::createStorageClustersResponseFromStorageCluster);
    }

    @Override
    public Mono<ClustersListResponse> getAllStorageClusters(Integer pageNumber, Integer pageSize, SortingOrder order) {

        final Sort sort = switch (order) {
            case NEWEST -> Sort.by(Sort.Order.by("createdOn"));
            case OLDER -> Sort.by(Sort.Order.asc("createdOn"));
            case MAX_STORAGE_AVAILABLE -> Sort.by(Sort.Order.by("capacity"));
            case MINIMUM_STORAGE_AVAILABLE -> Sort.by(Sort.Order.asc("capacity"));
        };

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Flux<StorageClusters> clusters = storageClustersRepository.findAllClusters(pageable);
        return clusters
                .collectList()
                .map((clustersList) -> entityMapperService.createClusterListResponseFromStorageClustersList(clustersList, pageNumber, pageSize));
    }

    @Override
    public Mono<Long> deleteCluster(String clusterId) {
        return storageClustersRepository.deleteCluster(clusterId);
    }

    @Override
    public Mono<ClusterResponse> getClusterById(String clusterId) {
        return storageClustersRepository.findClusterById(clusterId).map(entityMapperService::createStorageClustersResponseFromStorageCluster);
    }
}
