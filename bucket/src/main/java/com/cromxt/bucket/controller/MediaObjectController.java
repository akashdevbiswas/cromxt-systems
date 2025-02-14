package com.cromxt.bucket.controller;


import com.cromxt.bucket.service.MediaObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@RestController
@RequestMapping("/api/v1/objects")
@RequiredArgsConstructor
public class MediaObjectController {

    private final MediaObjectService mediaObjectService;

    @GetMapping(value = "/{objectId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Flux<DataBuffer>> getObject(
            @PathVariable(name = "objectId") String objectId
    ) {
        return ResponseEntity.ok(mediaObjectService.getFile(objectId));
    }


    @DeleteMapping("/{objectId}")
    public ResponseEntity<Mono<Void>> deleteMedia(@PathVariable(name = "objectId") String objectId) {
        return ResponseEntity.accepted().body(mediaObjectService.deleteMedia(objectId));
    }


}
