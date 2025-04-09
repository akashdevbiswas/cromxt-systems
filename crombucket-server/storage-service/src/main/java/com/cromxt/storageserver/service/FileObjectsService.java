package com.cromxt.storageserver.service;


import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;

public interface FileObjectsService {
    Flux<DataBuffer> getFile(String fileName);
}
