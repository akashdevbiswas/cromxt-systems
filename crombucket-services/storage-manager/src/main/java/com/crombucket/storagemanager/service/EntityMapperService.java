package com.crombucket.storagemanager.service;

import com.crombucket.storagemanager.repository.Page;
import com.crombucket.storagemanager.dtos.requests.ClusterRequest;
import com.crombucket.storagemanager.dtos.requests.StorageNodeRequest;
import com.crombucket.storagemanager.dtos.response.ClusterResponse;
import com.crombucket.storagemanager.dtos.response.StorageNodeResponse;
import com.crombucket.storagemanager.entity.StorageClusters;
import com.crombucket.storagemanager.entity.StorageNode;

import java.util.List;

public interface EntityMapperService {

    StorageClusters createClusterEntityFromClusterRequest(ClusterRequest clusterRequest);

    ClusterResponse createStorageClustersResponseFromStorageCluster(StorageClusters savedCluster);

    StorageNode createStorageNodeFromNodeRequest(StorageNodeRequest nodeRequest);

    StorageNodeResponse createStorageResponseFromStorageNodeEntity(StorageNode storageNode);

    <T, K> Page<T> pageResponseBuilder(List<T> contentList, Page<K> page);
}
