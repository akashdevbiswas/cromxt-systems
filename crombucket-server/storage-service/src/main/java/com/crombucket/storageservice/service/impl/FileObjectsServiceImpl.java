package com.crombucket.storageservice.service.impl;

import com.crombucket.storageservice.exception.InvalidMediaData;
import com.crombucket.storageservice.models.FileObjects;
import com.crombucket.storageservice.service.FileObjectsService;
import com.crombucket.storageservice.service.FileService;
import io.netty.buffer.ByteBufAllocator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileObjectsServiceImpl implements FileObjectsService {

    private final FileService fileService;
    private final ResourceLoader resourceLoader;

    @Override
    public Flux<DataBuffer> getFile(String fileName) {

        Mono<FileObjects> mediaObjectsMono = fileService.getFileByFileName(fileName);

        return Flux.from(mediaObjectsMono).flatMap(
                mediaObject -> {
                    Resource resource = resourceLoader.getResource("file:" + mediaObject.getAbsolutePath());
                    if (!resource.exists()) {
                        log.error("File not found {}", fileName);
                        return Flux.error(new InvalidMediaData("File not found"));
                    }
                    NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(
                            ByteBufAllocator.DEFAULT);

                    try {
                        InputStream inputStream = resource.getInputStream();
                        return DataBufferUtils.readInputStream(() -> inputStream, nettyDataBufferFactory, 4096);
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                    return Flux.empty();
                }
        );
    }
}
