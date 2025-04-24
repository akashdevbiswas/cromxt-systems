package com.crombucket.storageservice.repository.impl;


import com.crombucket.storageservice.client.MediaManagerClient;
import com.crombucket.storageservice.constants.FileVisibility;
import com.crombucket.storageservice.dtos.UpdateMediaVisibilityRequest;
import com.crombucket.storageservice.models.FileObjects;
import com.crombucket.storageservice.repository.MediaRepository;
import com.crombucket.storageservice.service.AccessURLGenerator;
import com.crombucket.storageservice.service.impl.FileManagementGRPCService;
import com.crombucket.storageservice.service.impl.FileServiceImpl;
import com.crombucket.common.mediamanager.response.MediaObjects;
import com.cromxt.proto.files.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Profile({"local","local-docker","local-docker-dev"})
public class InMemoryMediaManagerClient implements MediaManagerClient, MediaRepository {

    //    This hashmap is a local database of media objects which are saved in the system.
    private static final Map<String, MediaObjects> MEDIA_OBJECTS_EXISTS = new HashMap<>();
    private final AccessURLGenerator accessURLGenerator;
    private final FileManagementGRPCService fileManagementGRPCService;


    public InMemoryMediaManagerClient(AccessURLGenerator accessURLGenerator,
                                      FileManagementGRPCService fileManagementGRPCService,
                                      FileServiceImpl fileServiceImpl) {

        this.accessURLGenerator = accessURLGenerator;
        this.fileManagementGRPCService = fileManagementGRPCService;
        fileServiceImpl.getAllAvailableFiles().doOnNext(fileObjects -> {
            String mediaId = createMediaId(fileObjects.getFileId());
            MediaObjects mediaObjectsFromFileObjects = createMediaObjectsFromFileObjects(mediaId, fileObjects);
            MEDIA_OBJECTS_EXISTS.put(mediaId, mediaObjectsFromFileObjects);
        }).subscribe();
    }

    @Override
    public Mono<String> createMediaObject(String clientId, FileVisibility visibility) {
        String uniqueId = "";

        while (uniqueId.isEmpty() || MEDIA_OBJECTS_EXISTS.containsKey(uniqueId)) {
            uniqueId = UUID.randomUUID().toString();
        }

        MEDIA_OBJECTS_EXISTS.put(
                uniqueId,
                MediaObjects.builder()
                        .mediaId(uniqueId)
                        .visibility(visibility.name())
                        .build()
        );
        return Mono.just(uniqueId);
    }

    @Override
    public Mono<MediaObjects> updateMediaUploadStatus(String mediaId, String accessUrl, FileObjects fileObjects) {
        MediaObjects mediaObjects = MEDIA_OBJECTS_EXISTS.get(mediaId);

        mediaObjects.setFileId(fileObjects.getFileId());
        mediaObjects.setFileSize(fileObjects.getFileSize());
        mediaObjects.setAccessUrl(accessUrl);
        mediaObjects.setVisibility(fileObjects.getVisibility().name());
        mediaObjects.setCreatedOn(LocalDate.now().toString());
        MEDIA_OBJECTS_EXISTS.remove(mediaId);
        String newId = createMediaId(fileObjects.getFileId());
        mediaObjects.setMediaId(newId);
        MEDIA_OBJECTS_EXISTS.put(newId, mediaObjects);
        return Mono.just(mediaObjects);
    }

    @Override
    public Mono<Void> deleteMedias(List<String> mediaIds) {
        return Flux.fromIterable(mediaIds).doOnNext(mediaId -> {
            MediaObjects mediaObjects = MEDIA_OBJECTS_EXISTS.get(mediaId);
            FileObject fileObject = FileObject.newBuilder().setFileId(mediaObjects.getFileId()).build();
            Mono<FileOperationResponse> fileOperationResponseMono = fileManagementGRPCService.deleteFile(fileObject);

            fileOperationResponseMono.doOnNext(fileOperationResponse -> {
                if (fileOperationResponse.getStatus() == OperationStatus.SUCCESS) {
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

            if (mediaObjects == null) {
                return Mono.empty();
            }
            Visibility visibility = getFileVisibility(updateMediaVisibilityRequest.visibility());

            UpdateVisibilityRequest updateVisibilityRequest = UpdateVisibilityRequest.newBuilder()
                    .setFileId(mediaObjects.getFileId())
                    .setVisibility(visibility)
                    .build();

            return fileManagementGRPCService.changeFileVisibility(Mono.just(updateVisibilityRequest)).flatMap(
                    fileOperationResponse -> {
                        if (fileOperationResponse.getStatus() == OperationStatus.SUCCESS) {
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
                .createdOn(LocalDate.now().toString())
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
            case PRIVATE -> FileVisibility.PRIVATE;
            case PROTECTED -> FileVisibility.PROTECTED;
            case PUBLIC -> FileVisibility.PUBLIC;
            case UNRECOGNIZED -> throw new IllegalArgumentException("Invalid visibility");
        };
    }

    private String createMediaId(String fileId) {
        int len = FileVisibility.PUBLIC.getAccessType().length() + 9;
        int index = fileId.lastIndexOf(".");
        return fileId.substring(len, index);
    }

}
