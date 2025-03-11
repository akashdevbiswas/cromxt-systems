package com.cromxt.storageserver.controller;


import com.cromxt.common.crombucket.bucketmanager.ClusterInformation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/buckets")
public class ClusterController {

    @GetMapping
    public ClusterInformation getClusterInformation(){
        return ClusterInformation.builder()
                .clusterId("1")
                .clusterName("local-cluster")
                .clusterRouterUrl("http://localhost:8080")
                .build();
    }
}
