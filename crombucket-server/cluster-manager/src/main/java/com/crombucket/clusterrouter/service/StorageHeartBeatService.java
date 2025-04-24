package com.crombucket.clusterrouter.service;

import com.crombucket.common.kafka.BucketHeartBeat;

public interface StorageHeartBeatService {

    void renewBucket(BucketHeartBeat bucketHeartBeat);
}
