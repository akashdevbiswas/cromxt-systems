package com.crombucket.storagemanager.controller;


import com.crombucket.common.ResponseBuilder;
import com.crombucket.storagemanager.repository.Page;
import com.crombucket.storagemanager.dtos.requests.StorageNodeRequest;
import com.crombucket.storagemanager.dtos.response.StorageNodeResponse;
import com.crombucket.storagemanager.service.StorageNodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/nodes")
@RequiredArgsConstructor
public class StorageNodeController {

    private final StorageNodeService storageNodeService;
    private final ResponseBuilder responseBuilder;


    @PostMapping
    public Mono<ResponseEntity<StorageNodeResponse>> createStorageNode(@RequestBody StorageNodeRequest nodeRequest){
        Mono<StorageNodeResponse> storageNodeMono = storageNodeService.createStorageNode(nodeRequest);
        return responseBuilder.buildResponseWithBody(storageNodeMono, HttpStatus.CREATED);
    }

    @GetMapping("/clusters/{clusterCode}")
    public Mono<ResponseEntity<Page<StorageNodeResponse>>> getAllClustersByClusterId(
            @PathVariable String clusterCode,
            @RequestParam(required = false,defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false,defaultValue = "20") Integer pageSize
    ){
        Mono<Page<StorageNodeResponse>> storageNodePage = storageNodeService.getAllStorageNodesByClusterCode(clusterCode,pageNumber,pageSize);
        return responseBuilder.buildResponseWithBody(storageNodePage,HttpStatus.OK);
    }

}
