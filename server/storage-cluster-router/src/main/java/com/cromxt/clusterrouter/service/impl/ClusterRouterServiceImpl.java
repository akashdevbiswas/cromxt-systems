package com.cromxt.clusterrouter.service.impl;


import com.cromxt.clusterrouter.client.BucketManagerClient;
import com.cromxt.clusterrouter.dtos.StorageServerResponse;
import com.cromxt.clusterrouter.exception.NoRouteConfigurationFoundException;
import com.cromxt.clusterrouter.service.StorageServer;
import com.cromxt.clusterrouter.service.ClusterRouterService;
import com.cromxt.clusterrouter.service.ClusterManagementService;
import com.cromxt.clusterrouter.service.StorageHeartBeatService;
import com.cromxt.common.crombucket.kafka.BucketHeartBeat;
import com.cromxt.common.crombucket.systemmanager.StorageServerRequest;
import com.cromxt.common.crombucket.routeing.BucketDetailsResponse;
import com.cromxt.common.crombucket.routeing.MediaDetails;
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
public class ClusterRouterServiceImpl implements ClusterRouterService, ClusterManagementService, StorageHeartBeatService {

    private static final Map<String, StorageServer> AVAILABLE_STORAGE_SERVERS = new HashMap<>();
    private static final Map<String, StorageServer> ONLINE_BUCKETS = new HashMap<>();
    private static final Queue<String> USAGE_QUEUE = new LinkedList<>();
    private static BucketIDSize bucketHaveLargeSpace = null;

    private final Integer loadFactor;
    private static int countOfRequests;


    public ClusterRouterServiceImpl(
            Environment environment,
            BucketManagerClient bucketManagerClient
    ) {
        Integer loadFactor = environment.getProperty("ROUTE_SERVICE_CONFIG_BUCKET_LOAD_COUNT", Integer.class);
        assert loadFactor != null;
        this.loadFactor = loadFactor;

        bucketManagerClient.getBucketObjects().doOnNext(storageServerRequest -> {
            if (!AVAILABLE_STORAGE_SERVERS.containsKey(storageServerRequest.getStorageServerId()))
                AVAILABLE_STORAGE_SERVERS.put(storageServerRequest.getStorageServerId(),
                        StorageServer.builder()
                                .id(storageServerRequest.getStorageServerId())
                                .hostName(storageServerRequest.getHostName())
                                .rpcPort(storageServerRequest.getRpcPort())
                                .build());
        }).subscribe();
    }


    @Override
    public Flux<StorageServerResponse> getALlOnlineRoutes() {
        Iterator<Map.Entry<String, StorageServer>> onlineBucketsIterator = ONLINE_BUCKETS.entrySet().iterator();

        List<StorageServerResponse> buckets = new ArrayList<>();
        while (onlineBucketsIterator.hasNext()) {
            StorageServer availableBucket = onlineBucketsIterator.next().getValue();

            buckets.add(StorageServerResponse.builder()
                    .hostName(availableBucket.getHostName())
                    .rpcPort(availableBucket.getRpcPort())
                    .lastRefreshTime(availableBucket.getLastRefreshTime())
                    .availableSpaceInBytes(availableBucket.getAvailableSpaceInBytes())
                    .build());
        }
        return Flux.fromIterable(buckets);
    }

    @Override
    public Mono<StorageServerRequest> addNewStorageServer(StorageServerRequest storageServerRequest) {
        StorageServer newStorageServer = StorageServer.builder()
                .id(storageServerRequest.getStorageServerId())
                .rpcPort(storageServerRequest.getRpcPort())
                .hostName(storageServerRequest.getHostName())
                .build();
        AVAILABLE_STORAGE_SERVERS.put(storageServerRequest.getStorageServerId(), newStorageServer);

        return Mono.just(getStorageServerRequest(newStorageServer));
    }

    @Override
    public Mono<StorageServerRequest> deleteStorageServer(String storageServerId) {
        boolean isStoragePresent = AVAILABLE_STORAGE_SERVERS.containsKey(storageServerId);
        if(!isStoragePresent) {
            return Mono.error(new NoRouteConfigurationFoundException("No storage server found with this id."));
        }
        StorageServer remove = AVAILABLE_STORAGE_SERVERS.remove(storageServerId);
        return Mono.just(getStorageServerRequest(remove));
    }

    @Override
    public Mono<StorageServerRequest> updateClusterRoutes(String storageServerId, StorageServerRequest storageServerRequest) {
        if(!AVAILABLE_STORAGE_SERVERS.containsKey(storageServerId)) {
            return Mono.error(new NoRouteConfigurationFoundException("No storage server found with this id."));
        }
        StorageServer storageServer = AVAILABLE_STORAGE_SERVERS.get(storageServerId);

        if(storageServerRequest.getRpcPort() != null) storageServer.setRpcPort(storageServerRequest.getRpcPort());
        if(storageServerRequest.getHostName() != null) storageServer.setHostName(storageServerRequest.getHostName());
        return Mono.just(getStorageServerRequest(storageServer));
    }

    @Override
    public Flux<StorageServerResponse> getAllAvailableRoutes() {
        Collection<StorageServer> availableStorageServers = AVAILABLE_STORAGE_SERVERS.values();
        return Flux.fromIterable(availableStorageServers.stream().map(this::getStorageServerResponse).toList());
    }


    @Override
    public void renewBucket(BucketHeartBeat bucketHeartBeat) {
        String bucketId = bucketHeartBeat.getBucketId();

        if (AVAILABLE_STORAGE_SERVERS.containsKey(bucketId)) {
            if (ONLINE_BUCKETS.containsKey(bucketId)) {
                StorageServer storageServer = ONLINE_BUCKETS.get(bucketId);
                storageServer.setAvailableSpaceInBytes(bucketHeartBeat.getAvailableSpaceInBytes());
                storageServer.setLastRefreshTime(System.currentTimeMillis());
            } else {
//                If Online buckets collection do not contain the current bucket.
                StorageServer availableBucket = AVAILABLE_STORAGE_SERVERS.get(bucketId);

                StorageServer newBucket = StorageServer.builder()
                        .id(bucketId)
                        .hostName(availableBucket.getHostName())
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


    @Scheduled(fixedRate = 15000)
    public void refreshBuckets() {
        Iterator<Map.Entry<String, StorageServer>> iterator = ONLINE_BUCKETS.entrySet().iterator();
        while (iterator.hasNext()) {
            StorageServer storageServer = iterator.next().getValue();
            if (System.currentTimeMillis() - storageServer.getLastRefreshTime() > 15000) {
                iterator.remove();
            }
        }
    }

    @Override
    public Mono<BucketDetailsResponse> getBucketDetails(MediaDetails mediaDetails) {
        String bucketID = null;

        if (USAGE_QUEUE.isEmpty()) {
            return Mono.error(new NoRouteConfigurationFoundException("No buckets present in the bucket queue."));
        }

        boolean isAvailableBucketSizeOne = AVAILABLE_STORAGE_SERVERS.size() == 1;

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
    private StorageServerRequest getStorageServerRequest(StorageServer storageServer) {
        return StorageServerRequest.builder()
                .storageServerId(storageServer.getId())
                .rpcPort(storageServer.getRpcPort())
                .hostName(storageServer.getHostName())
                .build();
    }

    private StorageServerResponse getStorageServerResponse(StorageServer storageServer){
        return StorageServerResponse.builder()
                .storageServerId(storageServer.getId())
                .rpcPort(storageServer.getRpcPort())
                .hostName(storageServer.getHostName())
                .lastRefreshTime(storageServer.getLastRefreshTime())
                .availableSpaceInBytes(storageServer.getAvailableSpaceInBytes())
                .build();
    }

    private Mono<BucketDetailsResponse> generateBucketDetailsOrError(String bucketId) {
        if (bucketId == null) {
            return Mono.error(new NoRouteConfigurationFoundException("Not Buckets found"));
        }
        return ONLINE_BUCKETS.containsKey(bucketId) ?
                Mono.fromCallable(() -> {
                    StorageServer bucket = AVAILABLE_STORAGE_SERVERS.get(bucketId);
                    return BucketDetailsResponse.builder()
                            .hostName(bucket.getHostName())
                            .rpcPort(bucket.getRpcPort())
                            .build();
                }) :
                Mono.error(new NoRouteConfigurationFoundException("Unable to fetch bucket details"));
    }


//    @Scheduled(fixedRate = 5000)
//    private void printCollectionStatus(){

    /// /        Debug method to monitor the buckets state comment the method in case of production.
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