package com.cromxt.bucket.client;

import com.cromxt.common.crombucket.bucketmanager.UsersBucketInfo;
import reactor.core.publisher.Mono;

public interface BucketServerClient {

    Mono<UsersBucketInfo> getBucketInfoByClientId(String clientId);
}
