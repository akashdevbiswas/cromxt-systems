package com.crombucket.clusterrouter.service.impl;

import com.crombucket.clusterrouter.service.StorageHeartBeatService;
import com.crombucket.common.kafka.BucketHeartBeat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class RouteHeartbeatObserver {

    private final StorageHeartBeatService storageHeartBeatService;


    @KafkaListener(topics = "${ROUTE_SERVICE_CONFIG_BUCKET_HEARTBEAT_TOPIC}" , containerFactory = "bucketsHeartbeatKafkaListenerContainerFactory")
    private void bucketListUpdated(BucketHeartBeat bucketHeartBeat) {
        storageHeartBeatService.renewBucket(bucketHeartBeat);
    }

}
