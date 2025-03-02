package com.cromxt.mediamanager.controller;


import com.cromxt.common.crombucket.dtos.mediamanager.requests.NewMediaRequest;
import com.cromxt.common.crombucket.dtos.mediamanager.response.MediaEntityDTO;
import com.cromxt.common.crombucket.dtos.mediamanager.response.NewMediaResponseDTO;
import com.cromxt.mediamanager.service.MediaClientService;
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
    public ResponseEntity<Mono<MediaEntityDTO>> createMedia(
            @RequestBody NewMediaRequest newMediaRequest
            ){
        return ResponseEntity.accepted().body(mediaClientService.createMedia(newMediaRequest));
    }

    @PutMapping
    public ResponseEntity<Mono<NewMediaResponseDTO>> updateMedia(){
        return null;
    }

    @DeleteMapping(value = "/{mediaId}")
    public ResponseEntity<Mono<Void>> deleteMedia(@PathVariable(name = "mediaId") String mediaId){
        return ResponseEntity.accepted().body(mediaClientService.deleteMediaById(mediaId));
    }

}
