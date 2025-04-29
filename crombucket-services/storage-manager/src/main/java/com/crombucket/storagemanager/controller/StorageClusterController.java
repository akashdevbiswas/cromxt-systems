package com.crombucket.storagemanager.controller;


import com.crombucket.common.ResponseBuilder;
import com.crombucket.storagemanager.dtos.requests.ClusterRequest;
import com.crombucket.storagemanager.dtos.response.ClusterResponse;
import com.crombucket.storagemanager.repository.Page;
import com.crombucket.storagemanager.service.StorageClusterService;
import com.crombucket.storagemanager.utility.ClusterSortingOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/clusters")
@RequiredArgsConstructor
public class StorageClusterController {

    private final StorageClusterService storageClusterService;
    private final ResponseBuilder responseBuilder;


    @PostMapping
    public Mono<ResponseEntity<ClusterResponse>> createClusters(@RequestBody ClusterRequest clusterRequest) {
        Mono<ClusterResponse> clusterResponseMono = storageClusterService.createNewCluster(clusterRequest);
        return responseBuilder.buildResponseWithBody(clusterResponseMono, HttpStatus.CREATED);
    }

    @GetMapping
    public Mono<ResponseEntity<Page<ClusterResponse>>> getAllClusters(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(required = false, defaultValue = "NEWEST") ClusterSortingOrder order) {

        Mono<Page<ClusterResponse>> clustersListResponseMono = storageClusterService.getAllStorageClusters(pageNumber, pageSize, order);
        return responseBuilder.buildResponseWithBody(clustersListResponseMono, HttpStatus.OK);
    }

    @GetMapping(value = "/{clusterCode}")
    public Mono<ResponseEntity<ClusterResponse>> getClusterById(@PathVariable String clusterCode) {
        Mono<ClusterResponse> clusterResponseMono = storageClusterService.getClusterById(clusterCode);
        return responseBuilder.buildResponseWithBody(clusterResponseMono, HttpStatus.OK);
    }

    @DeleteMapping("/{clusterCode}")
    public Mono<ResponseEntity<Void>> deleteCluster(@PathVariable String clusterCode) {
        return storageClusterService
                .deleteCluster(clusterCode)
                .flatMap(deleteCount->{
                    String message = "Number of objects deleted " + deleteCount;
                    return responseBuilder.buildEmptyResponse(Mono.empty(),HttpStatus.ACCEPTED,message);
                }).onErrorResume(err-> responseBuilder.buildEmptyResponse(Mono.error(err),HttpStatus.FORBIDDEN));
    }
}
