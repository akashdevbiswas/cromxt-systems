package com.cromxt.clusterrouter.service;

import com.cromxt.jwt.kafka.BucketHeartBeat;

public interface StorageHeartBeatService {

    void renewBucket(BucketHeartBeat bucketHeartBeat);
}
