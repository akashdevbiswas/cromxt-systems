package com.crombucket.storagemanager.service.impl;

import com.crombucket.storagemanager.dtos.requests.ClusterRequest;
import com.crombucket.storagemanager.dtos.requests.StorageNodeRequest;
import com.crombucket.storagemanager.dtos.response.ClusterResponse;
import com.crombucket.storagemanager.dtos.response.RegionResponse;
import com.crombucket.storagemanager.repository.Page;
import com.crombucket.storagemanager.dtos.response.StorageNodeResponse;
import com.crombucket.storagemanager.entity.Clusters;
import com.crombucket.storagemanager.entity.Regions;
import com.crombucket.storagemanager.entity.StorageNode;
import com.crombucket.storagemanager.service.EntityMapperService;

import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EntityMapperServiceImpl implements EntityMapperService {


    @Override
    public Clusters createClusterEntityFromClusterRequest(Regions region, ClusterRequest clusterRequest) {
        return Clusters.builder()
                .clusterCode(clusterRequest.clusterCode())
                .createdOn(LocalDate.now())
                .capacity(0L)
                .availableSpace(0L)
                .region(region)
                .build();
    }

    @Override
    public ClusterResponse createClustersResponseFromStorageCluster(Clusters savedCluster) {
        return new ClusterResponse(
                savedCluster.getId(),
                savedCluster.getClusterCode(),
                savedCluster.getCapacity()
        );
    }

    @Override
    public StorageNode createStorageNodeFromNodeRequest(StorageNodeRequest nodeRequest) {
        Clusters cluster = Clusters.builder().clusterCode(nodeRequest.clusterCode()).build();
        return StorageNode.builder()
                .createdAt(LocalDate.now())
                .storageCode(nodeRequest.storageNodeCode())
                .clusters(cluster)
                .createdAt(LocalDate.now())
                .clusterJoinedDate(LocalDate.now())
                .build();
    }


    @Override
    public StorageNodeResponse createStorageResponseFromStorageNodeEntity(StorageNode storageNode) {
        return new StorageNodeResponse(
                storageNode.getId(),
                storageNode.getStorageCode(),
                storageNode.getAvailableSpace(),
                storageNode.getCreatedAt(),
                storageNode.getClusterJoinedDate()
        );
    }

    @Override
    public <T, K> Page<T> pageResponseBuilder(List<T> contentList, Page<K> page) {
        return Page.<T>builder()
                .totalPages(page.getTotalPages())
                .pageSize(page.getPageSize())
                .isFirst(page.getIsFirst())
                .isLast(page.getIsLast())
                .content(contentList)
                .results(page.getResults())
                .currentPage(page.getCurrentPage())
                .build();
    }

    @Override
    public RegionResponse createRegionResponseFromRegions(Regions regions) {
        return new RegionResponse(regions.getRegionName(), regions.getRegionCode(), regions.getStartedOn());
    }


}
