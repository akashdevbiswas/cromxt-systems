package com.cromxt.bucket.controller;


import com.cromxt.bucket.dtos.MediaVisibility;
import com.cromxt.bucket.service.FileObjectsService;
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
public class MediaObjectsController {

    private final FileObjectsService fileObjectsService;

    @GetMapping(value = "/{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Flux<DataBuffer>> getPublicObject(
            @PathVariable String fileId
    ) {
        return ResponseEntity.ok(fileObjectsService.getFile(fileId));
    }

    @PatchMapping(value = "/{fileId}")
    public ResponseEntity<Mono<Void>> changeVisibility(@PathVariable String fileId,@RequestBody MediaVisibility visibility) {
        return ResponseEntity.accepted().body(fileObjectsService.changeFileVisibility(fileId, visibility));
    }


    @DeleteMapping("/{fileId}")
    public ResponseEntity<Mono<Void>> deleteMedia(@PathVariable String fileId) {
        return ResponseEntity.accepted().body(fileObjectsService.deleteMedia(fileId));
    }

}
