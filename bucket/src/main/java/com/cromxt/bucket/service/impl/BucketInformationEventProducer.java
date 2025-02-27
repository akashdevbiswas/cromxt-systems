package com.cromxt.bucket.service.impl;


import com.cromxt.common.crombucket.kafka.BucketHeartBeat;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Profile({"dev", "prod"})
public class BucketInformationEventProducer {

    private final KafkaTemplate<String, BucketHeartBeat> kafkaTemplate;
    private final String topic;
    private final String storagePath;
    private final String bucketId;

    public BucketInformationEventProducer(
            KafkaTemplate<String, BucketHeartBeat> kafkaTemplate,
            Environment environment) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = environment.getProperty("BUCKET_CONFIG_KAFKA_TOPIC_NAME", String.class, "buckets");
        this.storagePath = environment.getProperty("BUCKET_CONFIG_STORAGE_PATH", String.class, "C:/Users/akash/Downloads/root_bucket");
        this.bucketId = environment.getProperty("BUCKET_CONFIG_ID", String.class);

    }

    @Scheduled(fixedRate = 15000)
    public void sendStorageInformation(){
        kafkaTemplate.send(topic, getBucketInformation());
    }

    private BucketHeartBeat getBucketInformation(){
        File rootDirectory = new File(storagePath);
        long availableSpaceInBytes = rootDirectory.getFreeSpace();
        return BucketHeartBeat.builder()
                .bucketId(bucketId)
                .availableSpaceInBytes(availableSpaceInBytes)
                .build();
    }



}
