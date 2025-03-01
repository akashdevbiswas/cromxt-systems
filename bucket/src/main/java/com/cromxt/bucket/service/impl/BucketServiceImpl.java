package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.models.MediaObjects;
import com.cromxt.bucket.service.BucketService;
import com.cromxt.bucket.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;


@Service
@RequiredArgsConstructor
public class BucketServiceImpl implements BucketService {
    private final FileService fileService;

    @Override
    public Flux<MediaObjects> getAllAvailableMedias() {
        return fileService.getAllAvailableMedias();
    }

    @Override
    public Mono<MediaObjects> getMediaObject(String mediaId) {
        return fileService.getMediaObjectById(mediaId);
    }
}
