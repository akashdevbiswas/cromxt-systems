package com.cromxt.mediamanager.service;


import com.cromxt.common.crombucket.mediamanager.requests.MediaRequest;
import com.cromxt.common.crombucket.mediamanager.requests.UpdateMediaRequestDTO;
import reactor.core.publisher.Mono;


public interface MediaClientService {

    Mono<MediaEntityDTO> createMedia(MediaRequest clientId);

    Mono<MediaEntityDTO> updateMedia(String mediaId, UpdateMediaRequestDTO mediaUpdateRequest);

    Mono<Void> deleteMediaById(String mediaId);
}
