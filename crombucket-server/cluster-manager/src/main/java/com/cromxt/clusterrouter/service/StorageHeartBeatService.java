package com.cromxt.clusterrouter.service;

import com.cromxt.userservice.kafka.BucketHeartBeat;

public interface StorageHeartBeatService {

    void renewBucket(BucketHeartBeat bucketHeartBeat);
}
