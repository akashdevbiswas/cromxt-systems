package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.service.AccessURLGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Profile("local")
@RequiredArgsConstructor
@Service
public class LocalAccessUrlGenerator implements AccessURLGenerator {
    private final BucketInformationService bucketInformationService;

    @Override
    public Mono<String> generateAccessURL(String mediaId) {
        String hostName = "localhost";
        Integer port = bucketInformationService.getHttpPort();
        return Mono.just(String.format("http://%s:%s/%s", hostName, port, mediaId));
    }
}
