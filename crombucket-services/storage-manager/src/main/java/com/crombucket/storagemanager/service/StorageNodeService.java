package com.crombucket.storagemanager.service;


import com.crombucket.storagemanager.repository.Page;
import com.crombucket.storagemanager.dtos.requests.StorageNodeRequest;
import com.crombucket.storagemanager.dtos.response.StorageNodeResponse;
import reactor.core.publisher.Mono;

public interface StorageNodeService {
    Mono<StorageNodeResponse> createStorageNode(StorageNodeRequest nodeRequest);

    Mono<Page<StorageNodeResponse>> getAllStorageNodesByClusterCode(String clusterCode, Integer pageNumber, Integer pageSize);

    Mono<Long> deleteNode(String nodeCode);
}
