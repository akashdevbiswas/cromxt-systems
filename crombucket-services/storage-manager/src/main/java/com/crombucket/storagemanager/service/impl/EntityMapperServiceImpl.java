package com.crombucket.storagemanager.service.impl;

import com.crombucket.storagemanager.dtos.requests.ClusterRequest;
import com.crombucket.storagemanager.dtos.requests.PositionRequest;
import com.crombucket.storagemanager.dtos.response.ClusterResponse;
import com.crombucket.storagemanager.dtos.response.ClustersListResponse;
import com.crombucket.storagemanager.entity.StorageClusters;
import com.crombucket.storagemanager.service.EntityMapperService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EntityMapperServiceImpl implements EntityMapperService {


    @Override
    public StorageClusters createClusterEntityFromClusterRequest(ClusterRequest clusterRequest) {
        return StorageClusters.builder()
                .clusterCode(clusterRequest.clusterCode())
                .createdOn(LocalDate.now())
                .capacity(0L)
                .build();
    }

    @Override
    public ClusterResponse createStorageClustersResponseFromStorageCluster(StorageClusters savedCluster) {

        return new ClusterResponse(
            savedCluster.getId(),
                savedCluster.getClusterCode(),
                savedCluster.getCapacity()
        );
    }

    @Override
    public ClustersListResponse createClusterListResponseFromStorageClustersList(List<StorageClusters> clustersList, Integer pageNumber, Integer pageSize) {

        List<ClusterResponse> clusterResponseList = clustersList.stream().map(this::createStorageClustersResponseFromStorageCluster).toList();

        return new ClustersListResponse(
                pageNumber,
                clustersList.size(),
                clustersList.size()<pageSize,
                clusterResponseList
        );
    }



}
