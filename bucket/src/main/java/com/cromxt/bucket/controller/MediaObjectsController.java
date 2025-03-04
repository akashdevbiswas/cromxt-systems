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
@RequestMapping("/api/v1/objects/public")
@RequiredArgsConstructor
public class MediaObjectsController {

    private final MediaObjectService mediaObjectService;

    @GetMapping(value = "/{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Flux<DataBuffer>> getPublicObject(
            @PathVariable(name = "objectId") String fileId
    ) {
        return ResponseEntity.ok(mediaObjectService.getFile(fileId));
    }

    @GetMapping(value = "/{fileId}")
    public ResponseEntity<Flux<DataBuffer>> getPrivateObject(
            @PathVariable(name = "objectId") String fileId
    ){
        return ResponseEntity.ok(mediaObjectService.getFile(fileId));
    }

    @PatchMapping(value = "/{fileId}")
    public ResponseEntity<Mono<Void>> changeVisibility(@PathVariable String fileId,
                                                       @RequestParam(name = "visibility", defaultValue = "true", required = false) Boolean visibility) {
        return ResponseEntity.accepted().body(mediaObjectService.changeFileVisibility(fileId));
    }


    @DeleteMapping("/{fileId}")
    public ResponseEntity<Mono<Void>> deleteMedia(@PathVariable(name = "objectId") String fileId) {
        return ResponseEntity.accepted().body(mediaObjectService.deleteMedia(fileId));
    }

}
