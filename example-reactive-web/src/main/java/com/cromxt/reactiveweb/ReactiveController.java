package com.cromxt.reactiveweb;


import com.cromxt.toolkit.crombucket.FileVisibility;
import com.cromxt.toolkit.crombucket.clients.ReactiveCromBucketClient;
import com.cromxt.toolkit.crombucket.response.FileUploadResponse;
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
    public Mono<FileUploadResponse> uploadFile(@RequestPart(name = "file") FilePart filePart,
                                               @RequestHeader("Content-Length") Long contentLength,
                                               @RequestParam(value = "visibility", required = false, defaultValue = "PUBLIC") FileVisibility visibility) throws IOException {
        return reactiveCromBucketClient.saveFile(filePart, contentLength, visibility);
    }
}
