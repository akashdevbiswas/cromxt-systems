package com.cromxt.bucket.client;

import com.cromxt.bucket.models.FileObjects;
import com.cromxt.common.crombucket.mediamanager.response.MediaObjects;
import reactor.core.publisher.Mono;


public interface MediaManagerClient {
    public abstract Mono<String> createMediaObject(String clientId);
    public abstract Mono<MediaObjects> updateMediaUploadStatus(String mediaId,String accessUrl ,FileObjects fileObjects);
}
