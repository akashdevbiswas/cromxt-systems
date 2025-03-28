package com.cromxt.clusterrouter.service;


import com.cromxt.clusterrouter.dtos.StorageServerResponse;
import com.cromxt.common.crombucket.systemmanager.StorageServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClusterManagementService {
    Flux<StorageServerResponse> getALlOnlineRoutes();
    Mono<StorageServerRequest> addNewStorageServer(StorageServerRequest storageServerRequest);
    Mono<StorageServerRequest> deleteStorageServer(String storageServerId);
    Mono<StorageServerRequest> updateClusterRoutes(String storageServerId, StorageServerRequest storageServerRequest);
    Flux<StorageServerResponse> getAllAvailableRoutes();
}
