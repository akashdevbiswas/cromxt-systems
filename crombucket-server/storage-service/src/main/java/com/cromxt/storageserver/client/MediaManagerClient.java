package com.cromxt.storageserver.client;

import com.cromxt.storageserver.constants.FileVisibility;
import com.cromxt.storageserver.models.FileObjects;
import com.cromxt.userservice.mediamanager.response.MediaObjects;
import reactor.core.publisher.Mono;


public interface MediaManagerClient {
    public abstract Mono<String> createMediaObject(String clientId, FileVisibility visibility);
    public abstract Mono<MediaObjects> updateMediaUploadStatus(String mediaId,String accessUrl ,FileObjects fileObjects);
}
