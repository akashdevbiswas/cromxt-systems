package com.crombucket.storagemanager.service;

import com.crombucket.storagemanager.dtos.requests.ClusterRequest;
import com.crombucket.storagemanager.dtos.requests.PositionRequest;
import com.crombucket.storagemanager.dtos.response.ClusterResponse;
import com.crombucket.storagemanager.dtos.response.ClustersListResponse;
import com.crombucket.storagemanager.entity.StorageClusters;

import java.util.List;

public interface EntityMapperService {
    
    StorageClusters createClusterEntityFromClusterRequest(ClusterRequest clusterRequest);

    ClusterResponse createStorageClustersResponseFromStorageCluster(StorageClusters savedCluster);

    ClustersListResponse createClusterListResponseFromStorageClustersList(List<StorageClusters> clustersList, Integer pageSize, Integer pageNumber);

}
