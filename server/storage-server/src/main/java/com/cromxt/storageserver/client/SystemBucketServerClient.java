package com.cromxt.storageserver.client;


import com.cromxt.storageserver.service.impl.BucketInformationService;
import com.cromxt.common.crombucket.bucketmanager.UsersBucketInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Profile("local")
@RequiredArgsConstructor
@Service
public class SystemBucketServerClient implements BucketServerClient {


    private final BucketInformationService bucketInformationService;

    @Override
    public Mono<UsersBucketInfo> getBucketInfoByClientId(String clientId) {
        return Mono.just(UsersBucketInfo.builder()
                .availableSpace(bucketInformationService.getAvailableSpace())
                .build());
    }
}
