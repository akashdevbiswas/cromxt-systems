package com.cromxt.bucket.service;

import com.cromxt.proto.files.MediaUploadRequest;
import com.cromxt.proto.files.MediaUploadResponse;
import com.cromxt.proto.files.ReactorMediaHandlerServiceGrpc;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class GRPCMediaService extends ReactorMediaHandlerServiceGrpc.MediaHandlerServiceImplBase{
    @Override
    abstract public Mono<MediaUploadResponse> uploadFile(Flux<MediaUploadRequest> request);
}
