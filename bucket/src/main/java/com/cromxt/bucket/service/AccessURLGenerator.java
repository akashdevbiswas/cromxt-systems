package com.cromxt.bucket.service;

import reactor.core.publisher.Mono;

public interface AccessURLGenerator {
    String generateAccessURL(String fileName);
}
