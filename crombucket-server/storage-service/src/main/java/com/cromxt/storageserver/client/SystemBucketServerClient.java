package com.cromxt.storageserver.client;


import com.cromxt.storageserver.service.impl.BucketInformationService;
import com.cromxt.userservice.bucketservice.UsersBucketInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Profile({"local","local-docker","local-docker-dev"})
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
