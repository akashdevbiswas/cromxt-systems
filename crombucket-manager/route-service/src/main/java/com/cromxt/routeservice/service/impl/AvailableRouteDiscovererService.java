package com.cromxt.routeservice.service.impl;

import com.cromxt.common.crombucket.kafka.BucketHeartBeat;
import com.cromxt.common.crombucket.kafka.BucketObject;
import com.cromxt.common.crombucket.kafka.BucketUpdateRequest;
import com.cromxt.routeservice.client.SystemManagerClient;
import com.cromxt.routeservice.service.BucketInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class AvailableRouteDiscovererService {

    private final BucketManager bucketManager;


    public AvailableRouteDiscovererService(
            BucketManager bucketManager
    ) {
        this.bucketManager = bucketManager;
    }

    @KafkaListener(topics = "${ROUTE_SERVICE_CONFIG_BUCKET_HEARTBEAT_TOPIC}", containerFactory = "bucketsHeartbeatKafkaListenerContainerFactory")
    private void bucketListUpdated(BucketHeartBeat bucketHeartBeat) {
        bucketManager.renewBucket(bucketHeartBeat);
    }

    @KafkaListener(topics = "${ROUTE_SERVICE_CONFIG_BUCKET_INFORMATION_UPDATE_TOPIC}", containerFactory = "bucketsUpdateKafkaListenerContainerFactory")
    private void bucketListUpdated(BucketUpdateRequest bucketUpdateRequest) {
        bucketManager.updateBucket(bucketUpdateRequest);
    }

}
