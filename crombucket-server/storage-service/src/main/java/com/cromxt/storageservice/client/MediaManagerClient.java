package com.cromxt.storageservice.client;

import com.cromxt.storageservice.constants.FileVisibility;
import com.cromxt.storageservice.models.FileObjects;
import com.cromxt.crombucket.mediamanager.response.MediaObjects;
import reactor.core.publisher.Mono;


public interface MediaManagerClient {
    public abstract Mono<String> createMediaObject(String clientId, FileVisibility visibility);
    public abstract Mono<MediaObjects> updateMediaUploadStatus(String mediaId,String accessUrl ,FileObjects fileObjects);
}
