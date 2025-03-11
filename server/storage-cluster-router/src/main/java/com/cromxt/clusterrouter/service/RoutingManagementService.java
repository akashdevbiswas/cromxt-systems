package com.cromxt.clusterrouter.service;


import com.cromxt.common.crombucket.kafka.BucketHeartBeat;
import com.cromxt.common.crombucket.kafka.BucketUpdateRequest;

public interface RoutingManagementService {


    void renewBucket(BucketHeartBeat bucketHeartBeat);

    void updateBucket(BucketUpdateRequest bucketUpdateRequest);
}
