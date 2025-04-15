package com.cromxt.storageservice.client;


import com.cromxt.storageservice.service.impl.StorageServerDetails;
import com.cromxt.crombucket.bucketservice.UsersBucketInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Profile({"local","local-docker","local-docker-dev"})
@RequiredArgsConstructor
@Service
public class SystemBucketServerClient implements BucketServerClient {


    private final StorageServerDetails storageServerDetails;

    @Override
    public Mono<UsersBucketInfo> getBucketInfoByClientId(String clientId) {
        return Mono.just(UsersBucketInfo.builder()
                .availableSpace(storageServerDetails.getAvailableSpace())
                .build());
    }
}
