package com.cromxt.clusterrouter.service;

import com.cromxt.crombucket.kafka.BucketHeartBeat;

public interface StorageHeartBeatService {

    void renewBucket(BucketHeartBeat bucketHeartBeat);
}
