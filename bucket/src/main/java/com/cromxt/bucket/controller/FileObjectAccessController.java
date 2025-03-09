package com.cromxt.bucket.controller;


import com.cromxt.bucket.service.FileObjectsService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/files")
public class FileObjectAccessController {

    private final FileObjectsService fileObjectsService;

    @GetMapping(value = "/{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Flux<DataBuffer>> getPublicObject(
            @PathVariable String fileId
    ) {
        return ResponseEntity.ok(fileObjectsService.getFile(fileId));
    }
}
