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
import com.cromxt.routeservice.service.Buckets;
import com.cromxt.routeservice.service.RoutingAdminServices;
import com.cromxt.routeservice.service.RoutingManagerService;
import com.cromxt.routeservice.service.RoutingService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class BucketManager implements RoutingAdminServices, RoutingManagerService, RoutingService {

    private static final Map<String, Buckets> AVAILABLE_BUCKETS = new HashMap<>();
    private static final Map<String, Buckets> ONLINE_BUCKETS = new HashMap<>();
    private static final Queue<String> USAGE_QUEUE = new LinkedList<>();
    private static BucketIDSize bucketHaveLargeSpace = null;

    private final Integer loadFactor;
    private static int countOfRequests;


    public BucketManager(
            Environment environment,
            SystemManagerClient systemManagerClient
    ) {
        Integer loadFactor = environment.getProperty("ROUTE_SERVICE_CONFIG_BUCKET_LOAD_COUNT", Integer.class);
        assert loadFactor != null;
        this.loadFactor = loadFactor;

        systemManagerClient.getBucketObjects().doOnNext(bucketObject -> {
            if(!AVAILABLE_BUCKETS.containsKey(bucketObject.getBucketId()))
                AVAILABLE_BUCKETS.put(bucketObject.getBucketId(),
                        Buckets.builder()
                                .bucketId(bucketObject.getBucketId())
                                .hostName(bucketObject.getHostName())
                                .httpPort(bucketObject.getHttpPort())
                                .rpcPort(bucketObject.getRpcPort())
                                .build());
        }).subscribe();
    }


    @Override
    public Flux<BucketInformationDTO> getAllOnlineBuckets() {
        Iterator<Map.Entry<String, Buckets>> onlineBucketsIterator = ONLINE_BUCKETS.entrySet().iterator();

        List<BucketInformationDTO> buckets = new ArrayList<>();
        while (onlineBucketsIterator.hasNext()) {
            Buckets availableBucket = onlineBucketsIterator.next().getValue();

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


    @Override
    public void renewBucket(BucketHeartBeat bucketHeartBeat) {
        String bucketId = bucketHeartBeat.getBucketId();

        if (AVAILABLE_BUCKETS.containsKey(bucketId)) {
            if (ONLINE_BUCKETS.containsKey(bucketId)) {
                Buckets buckets = ONLINE_BUCKETS.get(bucketId);
                buckets.setAvailableSpaceInBytes(bucketHeartBeat.getAvailableSpaceInBytes());
                buckets.setLastRefreshTime(System.currentTimeMillis());
            } else {
//                If Online buckets collection do not contain the current bucket.
                Buckets availableBucket = AVAILABLE_BUCKETS.get(bucketId);

                Buckets newBucket = Buckets.builder()
                        .bucketId(bucketId)
                        .hostName(availableBucket.getHostName())
                        .httpPort(availableBucket.getHttpPort())
                        .rpcPort(availableBucket.getRpcPort())
                        .availableSpaceInBytes(bucketHeartBeat.getAvailableSpaceInBytes())
                        .lastRefreshTime(System.currentTimeMillis())
                        .build();
                ONLINE_BUCKETS.put(bucketId, newBucket);
                USAGE_QUEUE.add(bucketId);
//                Update the bucketHaveLargeSpace variable if the current bucket heartbeat bucket have larger space than the previous large bucket.
                Long largeSpaceTillNow = bucketHaveLargeSpace.getAvailableSpace();

                if (largeSpaceTillNow < bucketHeartBeat.getAvailableSpaceInBytes()) {
                    bucketHaveLargeSpace = new BucketIDSize(bucketId, largeSpaceTillNow);
                }
            }
        }
    }

    @Override
    public void updateBucket(BucketUpdateRequest bucketUpdateRequest) {

        Method method = bucketUpdateRequest.getMethod();
        BucketObject bucketObject = bucketUpdateRequest.getNewBucketObject();

        switch (method) {
            case ADD:
                AVAILABLE_BUCKETS.put(bucketObject.getBucketId(),
                        new Buckets(
                                bucketObject.getBucketId(),
                                bucketObject.getHostName(),
                                bucketObject.getRpcPort(),
                                bucketObject.getHttpPort(),
                                null,
                                null)
                );
                break;
            case UPDATE:
                AVAILABLE_BUCKETS.replace(bucketObject.getBucketId(),
                        new Buckets(
                                bucketObject.getBucketId(),
                                bucketObject.getHostName(),
                                bucketObject.getRpcPort(),
                                bucketObject.getHttpPort(),
                                null,
                                null));
            case DELETE:
                AVAILABLE_BUCKETS.remove(bucketObject.getBucketId());
            default:
                break;
        }
    }


    @Scheduled(fixedRate = 15000)
    public void refreshBuckets() {
        Iterator<Map.Entry<String, Buckets>> iterator = ONLINE_BUCKETS.entrySet().iterator();
        while (iterator.hasNext()) {
            Buckets buckets = iterator.next().getValue();
            if (System.currentTimeMillis() - buckets.getLastRefreshTime() > 15000) {
                iterator.remove();
            }
        }
    }

    @Override
    public Mono<BucketDetails> getBucketDetails(MediaDetails mediaDetails) {
        String bucketID = null;

        if (USAGE_QUEUE.isEmpty()) {
            return Mono.error(new BucketError("No buckets present in the bucket queue."));
        }

        boolean isAvailableBucketSizeOne = AVAILABLE_BUCKETS.size() == 1;

        if (isAvailableBucketSizeOne) {
            /*
             * if Available bucket size is 1 then return that bucket with check that the bucket is alive or not.
             * If the bucket is not Online then return error.
             * If present then return that bucket.
             */
            bucketID = USAGE_QUEUE.peek();
        } else {
//                    If there are multiple buckets present.
            if (countOfRequests == loadFactor) {
//                    When the number of requestCount is equal to load factor.
                countOfRequests = 0;
                return generateBucketDetailsOrError(bucketHaveLargeSpace.getBucketId());
            }
//                    Find a bucketId from the USAGE_QUE until find a valid bucket.
            while (!USAGE_QUEUE.isEmpty()) {
                String tempBucketId = USAGE_QUEUE.poll();
                if (ONLINE_BUCKETS.containsKey(tempBucketId)) {
                    bucketID = tempBucketId;
                    countOfRequests++;
                    USAGE_QUEUE.add(tempBucketId);
                    break;
                }
            }

        }
        return generateBucketDetailsOrError(bucketID);
    }

    private Mono<BucketDetails> generateBucketDetailsOrError(String bucketId) {
        if (bucketId == null) {
            return Mono.error(new BucketError("Not Buckets found"));
        }
        return ONLINE_BUCKETS.containsKey(bucketId) ?
                Mono.fromCallable(() -> {
                    Buckets bucket = AVAILABLE_BUCKETS.get(bucketId);
                    return BucketDetails.builder()
                            .bucketId(bucket.getBucketId())
                            .hostName(bucket.getHostName())
                            .httpPort(bucket.getHttpPort())
                            .rpcPort(bucket.getRpcPort())
                            .build();
                }) :
                Mono.error(new BucketError("Unable to fetch bucket details"));
    }


//    @Scheduled(fixedRate = 5000)
//    private void printCollectionStatus(){
////        Debug method to monitor the buckets state comment the method in case of production.
//        System.out.println(ONLINE_BUCKETS.toString());
//        System.out.println(USAGE_QUEUE.toString());
//        System.out.println(AVAILABLE_BUCKETS.toString());
//    }



    @Setter
    @Getter
    @AllArgsConstructor
    static class BucketIDSize {
        String bucketId;
        Long availableSpace;
    }

}