package com.cromxt.mediaserver.service;


import com.cromxt.common.crombucket.dtos.mediaserver.requests.NewMediaRequest;
import com.cromxt.common.crombucket.dtos.mediaserver.requests.UpdateMediaRequest;
import reactor.core.publisher.Mono;

public interface MediaClientService {

    Mono<String> createMedia(NewMediaRequest newMediaRequest);

    Mono<Void> updateMedia(String mediaId, UpdateMediaRequest mediaRequest);

    Mono<Void> deleteMediaById(String mediaId);
}
