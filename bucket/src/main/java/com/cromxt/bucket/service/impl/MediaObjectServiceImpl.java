package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.exception.InvalidMediaData;
import com.cromxt.bucket.models.MediaObjects;
import com.cromxt.bucket.service.FileService;
import com.cromxt.bucket.service.MediaObjectService;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaObjectServiceImpl implements MediaObjectService {

    private final FileService fileService;
    private final ResourceLoader resourceLoader;

    @Override
    public Flux<DataBuffer> getFile(String mediaId) {

        Mono<MediaObjects> mediaObjectsMono = fileService.getMediaObjectById(mediaId);

        return Flux.from(mediaObjectsMono).flatMap(
                mediaObject -> {

                    Resource resource = resourceLoader.getResource("file:" + mediaObject.getAbsolutePath());
                    if (!resource.exists()) {
                        log.error("File not found {}", mediaId);
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

    @Override
    public Mono<Void> deleteMedia(String objectId) {
        return Mono.create((sink) -> {
            Resource resource = resourceLoader.getResource("file:" + objectId);
            try {
                File file = resource.getFile();
                file.delete();
                sink.success();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                sink.error(e);
            }
        });
    }


}
