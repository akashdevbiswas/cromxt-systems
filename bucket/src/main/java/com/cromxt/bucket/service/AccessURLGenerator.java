package com.cromxt.bucket.service;

import reactor.core.publisher.Mono;

public interface AccessURLGenerator {
    Mono<String> generateAccessURL(String mediaId);
}
