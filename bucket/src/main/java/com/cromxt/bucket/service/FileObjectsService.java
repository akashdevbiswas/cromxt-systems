package com.cromxt.bucket.service;


import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileObjectsService {
    Flux<DataBuffer> getFile(String fileName);
    Mono<Void> deleteMedia(String objectId);
    Mono<Void> changeFileVisibility(String objectId);
}
