package com.cromxt.bucket.controller;


import com.cromxt.bucket.dtos.UpdateMediaVisibilityRequest;
import com.cromxt.common.crombucket.mediamanager.response.MediaObjects;
import com.cromxt.bucket.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@Profile("local")
@RequestMapping(value = "/api/v1/medias")
@RequiredArgsConstructor
public class LocalMediaManagerController {

    private final MediaRepository mediaRepository;


    @GetMapping
    public Flux<MediaObjects> getMedias(){
        return mediaRepository.getAllAvailableMedias();
    }

    @GetMapping(value = "/{mediaId}")
    public Mono<MediaObjects> getMedia(@PathVariable String mediaId){
        return mediaRepository.getMediaObjectById(mediaId);
    }

    @DeleteMapping
    public Mono<Void> deleteMedia(@RequestBody List<String> mediaIds) {
        return mediaRepository.deleteMedias(mediaIds);
    }
    @PutMapping
    public Flux<MediaObjects> changeVisibility(@RequestBody List<UpdateMediaVisibilityRequest> updateMediaVisibilityRequests){
        return mediaRepository.updateMediasVisibility(updateMediaVisibilityRequests);
    }
}
