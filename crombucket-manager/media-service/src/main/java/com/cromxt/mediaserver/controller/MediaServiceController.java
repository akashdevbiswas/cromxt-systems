package com.cromxt.mediaserver.controller;

import com.cromxt.common.crombucket.dtos.mediaserver.requests.NewMediaRequest;
import com.cromxt.common.crombucket.dtos.mediaserver.requests.UpdateMediaRequest;
import com.cromxt.mediaserver.repository.MediaRepository;
import com.cromxt.mediaserver.service.MediaClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/service/v1/medias")
@RequiredArgsConstructor
public class MediaServiceController {

    private final MediaClientService mediaClientService;

    @PostMapping
    public ResponseEntity<Mono<String>> createMedia(
            @RequestBody NewMediaRequest mediaRequest
            ){
        return ResponseEntity.accepted().body(mediaClientService.createMedia(mediaRequest));
    }

    @PutMapping(value = "/{mediaId}")
    public ResponseEntity<Mono<Void>> updateMedia(
            @PathVariable(name = "mediaId") String mediaId,
            @RequestBody UpdateMediaRequest updateMediaRequest
            ){
        return ResponseEntity.accepted().body(mediaClientService.updateMedia(mediaId,updateMediaRequest));
    }

    @DeleteMapping(value = "/{mediaId}")
    public ResponseEntity<Mono<Void>> deleteMedia(@PathVariable(name = "mediaId") String mediaId){
        return ResponseEntity.accepted().body(mediaClientService.deleteMediaById(mediaId));
    }


}
