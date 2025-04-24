package com.crombucket.clusterrouter.controller;

import com.crombucket.clusterrouter.dtos.StorageServerResponse;
import com.crombucket.clusterrouter.service.ClusterManagementService;
import com.crombucket.common.systemmanager.StorageServerRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/clusters")
public record ClusterRoutingConfigureController(
        ClusterManagementService clusterManagementService
) {
    @GetMapping
    public Flux<StorageServerResponse> getAllOnlineBuckets() {
        return clusterManagementService.getALlOnlineRoutes();
    }

    @GetMapping(value = "/available-storage-routes")
    public Flux<StorageServerResponse> getAllAvailableRoutes(){
        return clusterManagementService.getAllAvailableRoutes();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<StorageServerRequest> addRoute(@RequestBody StorageServerRequest storageServerRequest) {
        return clusterManagementService.addNewStorageServer(storageServerRequest);
    }

    @PutMapping("/{storageId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<StorageServerRequest> updateRoute(@PathVariable String storageId, @RequestBody StorageServerRequest storageServerRequest) {
        return clusterManagementService.updateClusterRoutes(storageId,storageServerRequest);
    }

    @DeleteMapping("/{storageId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<StorageServerRequest> deleteRoute(@PathVariable String storageId) {
        return clusterManagementService.deleteStorageServer(storageId);
    }
}
