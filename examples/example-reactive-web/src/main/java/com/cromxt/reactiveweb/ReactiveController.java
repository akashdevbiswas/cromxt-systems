package com.cromxt.reactiveweb;


import com.cromxt.crombucket.sdk.FileVisibility;
import com.cromxt.crombucket.sdk.UpdateFileVisibilityRequest;
import com.cromxt.crombucket.sdk.clients.ReactiveCromBucketClient;
import com.cromxt.crombucket.sdk.response.FileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping(value = "/reactive")
@RequiredArgsConstructor
public class ReactiveController {

    private final ReactiveCromBucketClient reactiveCromBucketClient;


    @PostMapping
    public Mono<FileResponse> uploadFile(@RequestPart(name = "file") FilePart filePart,
                                         @RequestHeader("Content-Length") Long contentLength,
                                         @RequestParam(value = "visibility", required = false, defaultValue = "PUBLIC") FileVisibility visibility) throws IOException {
        return reactiveCromBucketClient.saveFile(filePart, contentLength, visibility);
    }

    @DeleteMapping(value = "/{mediaId}")
    public Mono<Void> changeVisibility(@PathVariable String mediaId) throws IOException {
        return reactiveCromBucketClient.delete(mediaId);
    }

    @PatchMapping
    public Mono<FileResponse> uploadFile(@RequestBody UpdateFileVisibilityRequest updateFileVisibilityRequest) throws IOException {
        return reactiveCromBucketClient.changeFileVisibility(updateFileVisibilityRequest);
    }
}
