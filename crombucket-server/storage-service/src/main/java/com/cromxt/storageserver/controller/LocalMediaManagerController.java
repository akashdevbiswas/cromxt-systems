package com.cromxt.storageserver.controller;


import com.cromxt.userservice.mediamanager.response.MediaObjects;
import com.cromxt.storageserver.dtos.UpdateMediaVisibilityRequest;
import com.cromxt.storageserver.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@Profile({"local","local-docker","local-docker-dev"})
@RequestMapping(value = "/media-manager/api/v1/medias")
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

    @PostMapping(value = "/delete")
    public Mono<Void> deleteMedia(@RequestBody List<String> mediaIds) {
        return mediaRepository.deleteMedias(mediaIds);
    }

    @PutMapping(value = "/change-visibility")
    public Flux<MediaObjects> changeVisibility(@RequestBody List<UpdateMediaVisibilityRequest> updateMediaVisibilityRequests){
        return mediaRepository.updateMediasVisibility(updateMediaVisibilityRequests);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<MediaObjects> updateVisibility(@RequestBody UpdateMediaVisibilityRequest updateMediaVisibilityRequest){
        Flux<MediaObjects> mediaResponseFlux = mediaRepository.updateMediasVisibility(List.of(updateMediaVisibilityRequest));
        return mediaResponseFlux.next();
    }

}
