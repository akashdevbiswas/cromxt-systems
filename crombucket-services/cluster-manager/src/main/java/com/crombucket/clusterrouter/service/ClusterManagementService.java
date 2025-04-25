package com.crombucket.clusterrouter.service;


import com.crombucket.clusterrouter.dtos.StorageServerResponse;
import com.crombucket.common.systemmanager.StorageServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClusterManagementService {
    Flux<StorageServerResponse> getALlOnlineRoutes();
    Mono<StorageServerRequest> addNewStorageServer(StorageServerRequest storageServerRequest);
    Mono<StorageServerRequest> deleteStorageServer(String storageServerId);
    Mono<StorageServerRequest> updateClusterRoutes(String storageServerId, StorageServerRequest storageServerRequest);
    Flux<StorageServerResponse> getAllAvailableRoutes();
}
