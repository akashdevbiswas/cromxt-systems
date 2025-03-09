package com.cromxt.bucket.controller;


import com.cromxt.bucket.dtos.UpdateMediaVisibilityRequest;
import com.cromxt.common.crombucket.mediamanager.response.MediaObjects;
import com.cromxt.bucket.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@Profile("local")
@RequestMapping(value = "/api/v1/medias")
@RequiredArgsConstructor
public class LocalMediaManagerController {

    private final MediaRepository mediaRepository;


    @DeleteMapping
    public Mono<Void> deleteMedia(List<String> mediaIds) {
        return mediaRepository.deleteMedias(mediaIds);
    }
    @PutMapping
    public Flux<MediaObjects> changeVisibility(List<UpdateMediaVisibilityRequest> updateMediaVisibilityRequests){
        return mediaRepository.updateMediasVisibility(updateMediaVisibilityRequests);
    }
}
