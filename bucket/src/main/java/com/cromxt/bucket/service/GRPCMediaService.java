package com.cromxt.bucket.service;

import com.cromxt.bucket.models.MediaObjects;
import com.cromxt.proto.files.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public abstract class GRPCMediaService extends ReactorMediaHandlerServiceGrpc.MediaHandlerServiceImplBase{
    protected MediaUploadResponse createNewSuccessMediaResponse(MediaObjects mediaObjects) {
        System.out.println(mediaObjects);
        return MediaUploadResponse.newBuilder()
                .setStatus(OperationStatus.SUCCESS)
                .setMediaObjectDetails(MediaObjectDetails.newBuilder()
                        .setMediaId(mediaObjects.getMediaId())
                        .setFileId(mediaObjects.getFileId())
                        .setAccessUrl(mediaObjects.getAccessUrl())
                        .setExtension(mediaObjects.getExtension())
                        .setCreatedOn(LocalDate.now().toString())
                        .setFileSize(mediaObjects.getFileSize())
                        .build()
                ).build();
    }
    protected MediaUploadResponse createNewErrorMediaResponse(String errorMessage) {
        return MediaUploadResponse.newBuilder()
                .setStatus(OperationStatus.ERROR)
                .setErrorMessage(errorMessage)
                .build();
    }

    @Override
    abstract public Mono<MediaUploadResponse> uploadFile(Flux<MediaUploadRequest> request);
}
