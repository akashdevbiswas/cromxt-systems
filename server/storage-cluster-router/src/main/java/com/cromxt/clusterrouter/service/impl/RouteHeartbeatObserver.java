package com.cromxt.clusterrouter.service.impl;

import com.cromxt.common.crombucket.kafka.BucketHeartBeat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class RouteHeartbeatObserver {

    private final ClusterRoutingManagementService clusterRoutingService;


    @KafkaListener(topics = "${ROUTE_SERVICE_CONFIG_BUCKET_HEARTBEAT_TOPIC}" , containerFactory = "bucketsHeartbeatKafkaListenerContainerFactory")
    private void bucketListUpdated(BucketHeartBeat bucketHeartBeat) {
        clusterRoutingService.renewBucket(bucketHeartBeat);
    }

}
