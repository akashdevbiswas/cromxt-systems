package com.cromxt.bucket.service;


import com.cromxt.bucket.models.MediaObjects;
import com.cromxt.proto.files.MediaUploadRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileService {
    Flux<MediaObjects> getAllAvailableMedias();

    Mono<MediaObjects> saveFile(String extension, Long spaceUserLeft, Flux<MediaUploadRequest> data);

    Mono<MediaObjects> getMediaObjectById(String mediaId);

    Mono<MediaObjects> deleteMediaObjectById(String mediaId);
}

