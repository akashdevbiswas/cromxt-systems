package com.crombucket.mediaservice.service;

import java.util.List;

import com.crombucket.common.mediaservice.requests.MediaObjectInitializeRequest;
import com.crombucket.common.mediaservice.response.MediaAddress;
import com.crombucket.common.mediaservice.response.MediaObjects;

import reactor.core.publisher.Mono;

public interface MediaService {
    Mono<MediaAddress> initializeMediaObject(MediaObjectInitializeRequest mediaObjectInitializeRequest);

    Mono<MediaObjects> getMediaObjectById(String mediaId);

    Mono<MediaObjects> updateVisibility(String mediaId, String visibility);

    Mono<MediaObjects> updateVisibility(List<String> mediaIds, String visibility);

    Mono<List<MediaObjects>> deleteMedias(List<String> mediaIds);

}
