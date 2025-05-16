package com.crombucket.storagemanager.service;

import com.crombucket.storagemanager.repository.Page;
import com.crombucket.storagemanager.dtos.requests.ClusterRequest;
import com.crombucket.storagemanager.dtos.requests.StorageNodeRequest;
import com.crombucket.storagemanager.dtos.response.ClusterResponse;
import com.crombucket.storagemanager.dtos.response.RegionResponse;
import com.crombucket.storagemanager.dtos.response.StorageNodeResponse;
import com.crombucket.storagemanager.entity.Clusters;
import com.crombucket.storagemanager.entity.Regions;
import com.crombucket.storagemanager.entity.StorageNode;

import java.util.List;

public interface EntityMapperService {

    Clusters createClusterEntityFromClusterRequest(Regions region, ClusterRequest clusterRequest);

    ClusterResponse createClustersResponseFromStorageCluster(Clusters savedCluster);

    StorageNode createStorageNodeFromNodeRequest(StorageNodeRequest nodeRequest);

    StorageNodeResponse createStorageResponseFromStorageNodeEntity(StorageNode storageNode);

    <T, K> Page<T> pageResponseBuilder(List<T> contentList, Page<K> page);

    RegionResponse createRegionResponseFromRegions(Regions regions);
}
