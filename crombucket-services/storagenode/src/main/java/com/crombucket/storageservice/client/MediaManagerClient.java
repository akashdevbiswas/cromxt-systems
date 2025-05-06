package com.crombucket.storageservice.client;

import com.crombucket.common.mediaservice.response.MediaObjects;
import com.crombucket.storageservice.constants.FileVisibility;
import com.crombucket.storageservice.models.FileObjects;

import reactor.core.publisher.Mono;


public interface MediaManagerClient {
    public abstract Mono<String> createMediaObject(String clientId, FileVisibility visibility);
    public abstract Mono<MediaObjects> updateMediaUploadStatus(String mediaId,String accessUrl ,FileObjects fileObjects);
}
