package com.cromxt.bucket.repository;

import com.cromxt.bucket.models.FileObjects;
import com.cromxt.bucket.models.MediaObjects;
import com.cromxt.bucket.service.AccessURLGenerator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
abstract public class MediaManager {
    protected final AccessURLGenerator accessURLGenerator;

    public abstract Mono<MediaObjects> createMediaObject(FileObjects newMediaRequest);
    public abstract Mono<Void> deleteMedia(String mediaId);
    public abstract Mono<MediaObjects> updateMedia(String mediaObjectId, FileObjects updateMediaDetails);
}
