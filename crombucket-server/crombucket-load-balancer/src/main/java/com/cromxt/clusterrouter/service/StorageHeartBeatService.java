package com.cromxt.clusterrouter.service;

import com.cromxt.common.crombucket.kafka.BucketHeartBeat;

public interface StorageHeartBeatService {

    void renewBucket(BucketHeartBeat bucketHeartBeat);
}
