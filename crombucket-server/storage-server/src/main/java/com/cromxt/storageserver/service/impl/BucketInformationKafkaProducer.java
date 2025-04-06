package com.cromxt.storageserver.service.impl;


import com.cromxt.common.crombucket.kafka.BucketHeartBeat;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Profile({"crombucket","crombucket-docker","crombucket-docker-dev"})
public class BucketInformationKafkaProducer {

    private final KafkaTemplate<String, BucketHeartBeat> kafkaTemplate;
    private final String topic;
    private final String bucketId;
    private final BucketInformationService bucketInformationService;

    public BucketInformationKafkaProducer(
            KafkaTemplate<String, BucketHeartBeat> kafkaTemplate,
            Environment environment,
            BucketInformationService bucketInformationService) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = environment.getProperty("BUCKET_CONFIG_KAFKA_TOPIC_NAME", String.class, "buckets");
        this.bucketId = environment.getProperty("BUCKET_CONFIG_ID", String.class);
        this.bucketInformationService = bucketInformationService;

    }

    @Scheduled(fixedRate = 15000)
    public void sendStorageInformation(){
        kafkaTemplate.send(topic, getBucketInformation());
    }

    private BucketHeartBeat getBucketInformation(){
        return BucketHeartBeat.builder()
                .bucketId(bucketId)
                .availableSpaceInBytes(bucketInformationService.getAvailableSpace())
                .build();
    }



}
