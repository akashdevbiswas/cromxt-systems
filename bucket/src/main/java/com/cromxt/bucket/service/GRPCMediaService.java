package com.cromxt.bucket.service;

import com.cromxt.bucket.models.MediaObjects;
import com.cromxt.common.crombucket.dtos.mediaserver.response.MediaEntityDTO;
import com.cromxt.proto.files.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public abstract class GRPCMediaService extends ReactorMediaHandlerServiceGrpc.MediaHandlerServiceImplBase{
    protected MediaUploadResponse createNewSuccessMediaResponse(MediaObjects mediaObjects) {
        return buildMediaUploadResponse(
                mediaObjects.getFileId(),
                mediaObjects.getFileId(),
                mediaObjects.getAccessUrl(),
                mediaObjects.getExtension(),
                mediaObjects.getFileSize()
        );
    }
    protected MediaUploadResponse createNewErrorMediaResponse(String errorMessage) {
        return MediaUploadResponse.newBuilder()
                .setStatus(OperationStatus.ERROR)
                .setErrorMessage(errorMessage)
                .build();
    }
    protected MediaUploadResponse createNewSuccessMediaResponse(MediaEntityDTO mediaEntityDTO) {
        return buildMediaUploadResponse(
                mediaEntityDTO.getMediaId(),
                mediaEntityDTO.getFileId(),
                mediaEntityDTO.getAccessUrl(),
                mediaEntityDTO.getExtension(),
                mediaEntityDTO.getFileSize()
        );
    }

    private MediaUploadResponse buildMediaUploadResponse(
            String mediaId,
            String fileId,
            String accessUrl,
            String extension,
            Long fileSize
    ){
        return MediaUploadResponse.newBuilder()
                .setStatus(OperationStatus.SUCCESS)
                .setMediaObjectDetails(MediaObjectDetails.newBuilder()
                        .setMediaId(mediaId)
                        .setFileId(fileId)
                        .setAccessUrl(accessUrl)
                        .setExtension(extension)
                        .setCreatedOn(LocalDate.now().toString())
                        .setFileSize(fileSize)
                        .build()
                ).build();
    }
    @Override
    abstract public Mono<MediaUploadResponse> uploadFile(Flux<MediaUploadRequest> request);
}
