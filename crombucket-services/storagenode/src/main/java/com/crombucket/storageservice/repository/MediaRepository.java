package com.crombucket.storageservice.repository;

import com.crombucket.common.mediaservice.response.MediaObjects;
import com.crombucket.storageservice.dtos.UpdateMediaVisibilityRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MediaRepository {

    Flux<MediaObjects> getAllAvailableMedias();

    Mono<MediaObjects> getMediaObjectById(String mediaId);

    Mono<Void> deleteMedias(List<String> mediaIds);

    Flux<MediaObjects> updateMediasVisibility(List<UpdateMediaVisibilityRequest> updateMediaVisibilityRequests);
}
