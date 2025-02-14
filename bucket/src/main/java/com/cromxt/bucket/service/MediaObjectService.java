package com.cromxt.bucket.service;


import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MediaObjectService {
    Flux<DataBuffer> getFile(String mediaId);
    Mono<Void> deleteMedia(String objectId);
}
