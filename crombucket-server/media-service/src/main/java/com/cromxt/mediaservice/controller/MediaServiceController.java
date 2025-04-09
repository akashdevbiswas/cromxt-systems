package com.cromxt.mediaservice.controller;


import com.cromxt.crombucket.mediamanager.requests.MediaRequest;
import com.cromxt.crombucket.mediamanager.response.UpdateMediaUploadStatusRequest;
import com.cromxt.mediaservice.service.MediaClientService;
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
            @RequestBody MediaRequest mediaRequest
            ){
        return ResponseEntity.accepted().body(mediaClientService.createMedia(mediaRequest));
    }

    @PutMapping
    public ResponseEntity<Mono<UpdateMediaUploadStatusRequest>> updateMedia(){
        return null;
    }

    @DeleteMapping(value = "/{mediaId}")
    public ResponseEntity<Mono<Void>> deleteMedia(@PathVariable(name = "mediaId") String mediaId){
        return ResponseEntity.accepted().body(mediaClientService.deleteMediaById(mediaId));
    }

}
