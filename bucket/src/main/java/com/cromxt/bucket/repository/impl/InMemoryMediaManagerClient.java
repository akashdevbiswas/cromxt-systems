package com.cromxt.bucket.repository.impl;


import com.cromxt.bucket.client.MediaManagerClient;
import com.cromxt.bucket.constants.FileVisibility;
import com.cromxt.bucket.dtos.UpdateMediaVisibilityRequest;
import com.cromxt.bucket.models.FileObjects;
import com.cromxt.bucket.repository.MediaRepository;
import com.cromxt.bucket.service.AccessURLGenerator;
import com.cromxt.bucket.service.impl.BucketInformationService;
import com.cromxt.bucket.service.impl.FileManagementGRPCService;
import com.cromxt.common.crombucket.mediamanager.response.MediaObjects;
import com.cromxt.proto.files.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Profile("local")
@RequiredArgsConstructor
public class InMemoryMediaManagerClient implements MediaManagerClient, MediaRepository {

    //    This hashmap is a local database of media objects which are saved in the system.
    private static final Map<String, MediaObjects> MEDIA_OBJECTS_EXISTS = new HashMap<>();
    private final BucketInformationService bucketInformationService;
    private final AccessURLGenerator accessURLGenerator;
    private final FileManagementGRPCService fileManagementGRPCService;


    @Override
    public Mono<String> createMediaObject(String clientId) {
        String uniqueId = "";

        while (uniqueId.isEmpty() || MEDIA_OBJECTS_EXISTS.containsKey(uniqueId)) {
            uniqueId = UUID.randomUUID().toString();
        }

        MEDIA_OBJECTS_EXISTS.put(uniqueId, MediaObjects.builder().mediaId(uniqueId).build());
        return Mono.just(uniqueId);
    }

    @Override
    public Mono<MediaObjects> updateMediaUploadStatus(String mediaId, String accessUrl, FileObjects fileObjects) {
        MediaObjects mediaObjects = MEDIA_OBJECTS_EXISTS.get(mediaId);

        mediaObjects.setFileId(fileObjects.getFileId());
        mediaObjects.setFileSize(fileObjects.getFileSize());
        mediaObjects.setAccessUrl(accessUrl);
        mediaObjects.setVisibility(fileObjects.getVisibility().name());
        MEDIA_OBJECTS_EXISTS.put(mediaId, mediaObjects);

        return Mono.just(mediaObjects);
    }

    @Override
    public Mono<Void> deleteMedias(List<String> mediaIds) {
        return Flux.fromIterable(mediaIds).doOnNext(mediaId -> {
            MediaObjects mediaObjects = MEDIA_OBJECTS_EXISTS.get(mediaId);
            FileObject fileObject = FileObject.newBuilder().setFileId(mediaObjects.getFileId()).build();
            Mono<FileOperationResponse> fileOperationResponseMono = fileManagementGRPCService.deleteFile(fileObject);

            fileOperationResponseMono.doOnNext(fileOperationResponse -> {
                if (fileOperationResponse.getStatus() != OperationStatus.SUCCESS) {
                    MEDIA_OBJECTS_EXISTS.remove(mediaId);
                }
            }).subscribe();

        }).then();
    }

    @Override
    public Flux<MediaObjects> updateMediasVisibility(List<UpdateMediaVisibilityRequest> updateMediaVisibilityRequests) {
        return Flux.fromIterable(updateMediaVisibilityRequests).flatMap(updateMediaVisibilityRequest -> {
            String mediaID = updateMediaVisibilityRequest.mediaId();
            MediaObjects mediaObjects = MEDIA_OBJECTS_EXISTS.get(mediaID);
            Visibility visibility = getFileVisibility(updateMediaVisibilityRequest.fileVisibility());

            UpdateVisibilityRequest updateVisibilityRequest = UpdateVisibilityRequest.newBuilder()
                    .setFileId(mediaObjects.getFileId())
                    .setVisibility(visibility)
                    .build();

            return fileManagementGRPCService.changeFileVisibility(Mono.just(updateVisibilityRequest)).flatMap(
                    fileOperationResponse -> {
                        if(fileOperationResponse.getStatus() == OperationStatus.SUCCESS) {
                            FileObjectDetails fileObjectDetails = fileOperationResponse.getFileObjectDetails();

                            FileVisibility updatedVisibility = getFileVisibility(fileObjectDetails.getVisibility());
                            mediaObjects.setVisibility(updatedVisibility.name());
                            mediaObjects.setFileId(fileObjectDetails.getFileId());
                            mediaObjects.setAccessUrl(fileObjectDetails.getAccessUrl());

                            MEDIA_OBJECTS_EXISTS.replace(mediaID, mediaObjects);
                            return Mono.just(mediaObjects);
                        }
                        return Mono.empty();
                    });
        });
    }

    @Override
    public Flux<MediaObjects> getAllAvailableMedias() {
        return Flux.fromIterable(MEDIA_OBJECTS_EXISTS.values());
    }

    @Override
    public Mono<MediaObjects> getMediaObjectById(String mediaId) {
        return Mono.just(MEDIA_OBJECTS_EXISTS.get(mediaId));
    }


    private MediaObjects createMediaObjectsFromFileObjects(String mediaId, FileObjects fileObjects) {
        String fileId = fileObjects.getFileId();
        String accessUrl = accessURLGenerator.generateAccessURL(fileId);
        return MediaObjects.builder()
                .mediaId(mediaId)
                .fileId(fileId)
                .visibility(fileObjects.getVisibility().name())
                .accessUrl(accessUrl)
                .fileSize(fileObjects.getFileSize())
                .build();
    }

    private Visibility getFileVisibility(String visibility) {
        return switch (visibility) {
            case "PRIVATE" -> Visibility.PRIVATE;
            case "PROTECTED" -> Visibility.PROTECTED;
            case "PUBLIC" -> Visibility.PUBLIC;
            default -> throw new IllegalArgumentException("Invalid visibility");
        };
    }
    private FileVisibility getFileVisibility(Visibility visibility) {
        return switch (visibility) {
            case PRIVATE -> FileVisibility.PRIVATE_ACCESS;
            case PROTECTED -> FileVisibility.PROTECTED_ACCESS;
            case PUBLIC -> FileVisibility.PUBLIC_ACCESS;
            case UNRECOGNIZED -> throw new IllegalArgumentException("Invalid visibility");
        };
    }

}
