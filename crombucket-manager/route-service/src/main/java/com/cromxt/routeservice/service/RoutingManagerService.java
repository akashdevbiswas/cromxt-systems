package com.cromxt.routeservice.service;


import com.cromxt.common.crombucket.kafka.BucketHeartBeat;
import com.cromxt.common.crombucket.kafka.BucketUpdateRequest;

public interface RoutingManagerService {


    void renewBucket(BucketHeartBeat bucketHeartBeat);

    void updateBucket(BucketUpdateRequest bucketUpdateRequest);
}
