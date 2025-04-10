package com.cromxt.storageservice.client;

import com.cromxt.crombucket.bucketservice.UsersBucketInfo;
import reactor.core.publisher.Mono;

public interface BucketServerClient {

    Mono<UsersBucketInfo> getBucketInfoByClientId(String clientId);
}
