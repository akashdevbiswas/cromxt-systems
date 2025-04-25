package com.crombucket.bucketservice.controller;


import com.crombucket.bucketservice.dtos.response.BucketGroupResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping(value = "/api/v1/bucket-groups")
public class BucketGroupsController {

    @PostMapping
    public Mono<BucketGroupResponse> createBucketGroup(){
        return null;
    }

}
