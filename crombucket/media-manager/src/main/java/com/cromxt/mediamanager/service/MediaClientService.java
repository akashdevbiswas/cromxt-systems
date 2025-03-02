package com.cromxt.mediamanager.service;


import com.cromxt.common.crombucket.dtos.mediamanager.requests.NewMediaRequest;
import com.cromxt.common.crombucket.dtos.mediamanager.requests.UpdateMediaRequestDTO;
import com.cromxt.common.crombucket.dtos.mediamanager.response.MediaEntityDTO;
import com.cromxt.common.crombucket.dtos.mediamanager.response.NewMediaResponseDTO;
import reactor.core.publisher.Mono;


public interface MediaClientService {

    Mono<MediaEntityDTO> createMedia(NewMediaRequest clientId);

    Mono<MediaEntityDTO> updateMedia(String mediaId, UpdateMediaRequestDTO mediaUpdateRequest);

    Mono<Void> deleteMediaById(String mediaId);
}
