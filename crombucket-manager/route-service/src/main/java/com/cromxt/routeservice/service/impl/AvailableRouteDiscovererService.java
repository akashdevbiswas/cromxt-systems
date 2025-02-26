package com.cromxt.routeservice.service.impl;

import com.cromxt.common.crombucket.kafka.BucketHeartBeat;
import com.cromxt.common.crombucket.kafka.BucketObject;
import com.cromxt.common.crombucket.kafka.BucketUpdateRequest;
import com.cromxt.common.crombucket.kafka.Method;
import com.cromxt.common.crombucket.routeing.BucketDetails;
import com.cromxt.common.crombucket.routeing.MediaDetails;
import com.cromxt.routeservice.client.SystemManagerClient;
import com.cromxt.routeservice.dtos.BucketInformationDTO;
import com.cromxt.routeservice.exception.BucketError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@Service
public class AvailableRouteDiscovererService {


    private static final Map<String, AvailableBucket> AVAILABLE_BUCKETS = new HashMap<>();
    private static final Map<String, AvailableBucket> ONLINE_BUCKETS = new HashMap<>();
    private static String lastUsedBucketId = null;
    private static String bucketHaveLargerSpaceTillNow = null;
    private static final Queue<String> bucketQue = new LinkedList<>();
    private final int bucketLoadCount;
    private static int bucketCount = 0;
    private static long api_hits = 0;


    public AvailableRouteDiscovererService(SystemManagerClient systemManagerClient, Environment environment) {
        Integer loadFactor = environment.getProperty("ROUTE_SERVICE_CONFIG_BUCKET_LOAD_COUNT", Integer.class);
        assert loadFactor != null;
        this.bucketLoadCount = loadFactor;
        systemManagerClient.getBucketObjects().doOnNext(bucketObject -> {
            AVAILABLE_BUCKETS.put(bucketObject.getBucketId(),
                    new AvailableBucket(
                            bucketObject.getBucketId(),
                            bucketObject.getHostName(),
                            bucketObject.getRpcPort(),
                            bucketObject.getHttpPort(),
                            System.currentTimeMillis(),
                            null)
            );
        }).subscribe();
    }

    @KafkaListener(topics = "${ROUTE_SERVICE_CONFIG_BUCKET_HEARTBEAT_TOPIC}", containerFactory = "bucketsHeartbeatKafkaListenerContainerFactory")
    private void bucketListUpdated(BucketHeartBeat bucketHeartBeat) {

        String bucketId = bucketHeartBeat.getBucketId();
        if (AVAILABLE_BUCKETS.containsKey(bucketId)) {
            AvailableBucket bucket = ONLINE_BUCKETS.get(bucketId);
            if (bucket == null) {
                bucket = AVAILABLE_BUCKETS.get(bucketId);
                bucketQue.add(bucketId);
            }
            bucket.setLastRefreshTime(System.currentTimeMillis());
            bucket.setAvailableSpaceInBytes(bucketHeartBeat.getAvailableSpaceInBytes());
            ONLINE_BUCKETS.put(bucketId, bucket);
            if (bucketHaveLargerSpaceTillNow == null) {
                bucketHaveLargerSpaceTillNow = bucketId;
            }
            Long largeSpace = ONLINE_BUCKETS.get(bucketHaveLargerSpaceTillNow).getAvailableSpaceInBytes();
            if (largeSpace == null) {
                return;
            }
            bucketHaveLargerSpaceTillNow = largeSpace > bucketHeartBeat.getAvailableSpaceInBytes() ? bucketHaveLargerSpaceTillNow : bucketId;
        }
    }

    @KafkaListener(topics = "${ROUTE_SERVICE_CONFIG_BUCKET_INFORMATION_UPDATE_TOPIC}", containerFactory = "bucketsUpdateKafkaListenerContainerFactory")
    private void bucketListUpdated(BucketUpdateRequest bucketUpdateRequest) {

        Method method = bucketUpdateRequest.getMethod();
        BucketObject bucketObject = bucketUpdateRequest.getNewBucketObject();

        switch (method) {
            case ADD:
                System.out.println("Executed");
                AVAILABLE_BUCKETS.put(bucketObject.getBucketId(),
                        new AvailableBucket(
                                bucketObject.getBucketId(),
                                bucketObject.getHostName(),
                                bucketObject.getRpcPort(),
                                bucketObject.getHttpPort(),
                                System.currentTimeMillis(),
                                null)
                );
                break;
            case UPDATE:
                AVAILABLE_BUCKETS.replace(bucketObject.getBucketId(),
                        new AvailableBucket(
                                bucketObject.getBucketId(),
                                bucketObject.getHostName(),
                                bucketObject.getRpcPort(),
                                bucketObject.getHttpPort(),
                                System.currentTimeMillis(),
                                null));
            case DELETE:
                AVAILABLE_BUCKETS.remove(bucketObject.getBucketId());
            default:
                break;
        }

    }

//    @Scheduled(fixedRate = 5000)
//    public void printBuckets() {
//        System.out.println("Online Buckets " + ONLINE_BUCKETS);
//    }
//    @Scheduled(fixedRate = 5000)
//    public void printBucketQue() {
//        for (String bucketId : bucketQue) {
//            System.out.print(bucketId+", ");
//        }
//    }


    @Scheduled(fixedRate = 15000)
    public void refreshRoutes() {
        Iterator<Map.Entry<String, AvailableBucket>> iterator = ONLINE_BUCKETS.entrySet().iterator();
        while (iterator.hasNext()) {
            AvailableBucket availableBucket = iterator.next().getValue();
            if (System.currentTimeMillis() - availableBucket.getLastRefreshTime() > 15000) {
                iterator.remove();
            }
        }
    }

    public Flux<BucketInformationDTO> getAllOnlineBuckets() {
        Iterator<Map.Entry<String, AvailableBucket>> iterator = ONLINE_BUCKETS.entrySet().iterator();
        List<BucketInformationDTO> buckets = new ArrayList<>();
        while (iterator.hasNext()) {
            AvailableBucket availableBucket = iterator.next().getValue();
            buckets.add(BucketInformationDTO.builder()
                    .bucketId(availableBucket.getBucketId())
                    .hostName(availableBucket.getHostName())
                    .rpcPort(availableBucket.getRpcPort())
                    .httpPort(availableBucket.getHttpPort())
                    .lastRefreshTime(availableBucket.getLastRefreshTime())
                    .availableSpaceInBytes(availableBucket.getAvailableSpaceInBytes())
                    .build());
        }
        return Flux.fromIterable(buckets);
    }

    public Mono<BucketDetails> getBucket(MediaDetails mediaDetails) {

//        TODO: This is a general implementation of the bucket selection logic.
//        This is just a temporary solution.
//        But it can be improve with the help of machine learning.

        api_hits++;

//        To manage the space in all buckets this code sends each second request to the bucket with larger space.

        boolean isAvailableBucketSizeOne = AVAILABLE_BUCKETS.size() == 1;

        if (bucketCount >= bucketLoadCount && (!Objects.equals(lastUsedBucketId, bucketHaveLargerSpaceTillNow) || isAvailableBucketSizeOne)) {
            AvailableBucket availableBucket = AVAILABLE_BUCKETS.get(bucketHaveLargerSpaceTillNow);
            lastUsedBucketId = bucketHaveLargerSpaceTillNow;
            bucketCount=0;
            log.info("Selected large bucket");

            if(!ONLINE_BUCKETS.containsKey(bucketHaveLargerSpaceTillNow)){
                return Mono.error(new RuntimeException("Bucket not found"));
            }
            return Mono.just(BucketDetails.builder()
                    .bucketId(availableBucket.getBucketId())
                    .rpcPort(availableBucket.getRpcPort())
                    .httpPort(availableBucket.getHttpPort())
                    .hostName(availableBucket.getHostName())
                    .build());
        }

        if(bucketQue.isEmpty()) {
            bucketQue.addAll(ONLINE_BUCKETS.keySet());
        }

        String generatedBucketId = null;

        String tempBucketId;
        while (!bucketQue.isEmpty()) {
            tempBucketId = bucketQue.poll();
            if (ONLINE_BUCKETS.containsKey(tempBucketId)) {
                generatedBucketId = tempBucketId;
                bucketQue.add(tempBucketId);
                break;
            }
        }

        if (generatedBucketId == null) { // Case 1
            log.error("No registered buckets found.");
            log.error("Number of api hits {}", api_hits);
            return Mono.error(new RuntimeException("No registered buckets found."));
        }

        AvailableBucket bucket = ONLINE_BUCKETS.get(generatedBucketId);


        log.info("Select from bucket que {}", bucketCount);
        bucketCount++;
        return Mono.just(BucketDetails.builder()
                .hostName(bucket.getHostName())
                .bucketId(bucket.getBucketId())
                .httpPort(bucket.getHttpPort())
                .rpcPort(bucket.getRpcPort())
                .build());
    }


    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class AvailableBucket {
        private String bucketId;
        private String hostName;
        private Integer rpcPort;
        private Integer httpPort;
        private Long lastRefreshTime;
        private Long availableSpaceInBytes;
    }

    //    Needs as JSON parser.
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    private static class BucketData {
        //        Add extra metadata if needed.
        List<Buckets> buckets;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class Buckets {
        String bucketId;
        String hostName;
        Integer port;
    }

}
