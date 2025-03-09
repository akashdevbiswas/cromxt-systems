package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.client.BucketServerClient;
import com.cromxt.bucket.client.MediaManagerClient;
import com.cromxt.bucket.constants.FileVisibility;
import com.cromxt.bucket.service.AccessURLGenerator;
import com.cromxt.bucket.service.FileService;
import com.cromxt.bucket.service.GRPCService;
import com.cromxt.common.crombucket.grpc.MediaHeadersKey;
import com.cromxt.common.crombucket.mediamanager.response.MediaObjects;
import com.cromxt.proto.files.*;
import io.grpc.Context;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
@Slf4j
public class  FileHandlerGRPCServiceImpl extends ReactorFileHandlerServiceGrpc.FileHandlerServiceImplBase implements GRPCService {

    private final FileService fileService;
    private final MediaManagerClient mediaManagerClient;
    private final BucketServerClient bucketServerClient;
    private final AccessURLGenerator accessURLGenerator;

    //    Handle the upload request in reactive way(Using reactive types Mono and Flux.)
    @Override
    public Mono<MediaUploadResponse> uploadFile(Flux<FileUploadRequest> request) {


        FileMetadata fileDetails = MediaHeadersKey.FILE_METADATA.getContextKey().get(Context.current());

        String clientId = fileDetails.getClientId();

        FileVisibility visibility = getFileVisibility(fileDetails.getVisibility());

        return bucketServerClient.getBucketInfoByClientId(clientId)
                .flatMap(bucketInfo -> mediaManagerClient.createMediaObject(clientId,visibility)
                        .flatMap(mediaId -> fileService.saveFile(
                                        clientId,
                                        fileDetails.getExtension(),
                                        bucketInfo.getAvailableSpace(),
                                        request, visibility)
                                .flatMap(fileObjects -> {
                                            String accessUrl = accessURLGenerator.generateAccessURL(fileObjects.getFileId());
                                            Mono<MediaObjects> mediaObject = mediaManagerClient.updateMediaUploadStatus(mediaId, accessUrl, fileObjects);

                                            return mediaObject.map(savedMediaObject -> {

                                                FileVisibility savedFileVisibility = FileVisibility.valueOf(savedMediaObject.getVisibility());

                                                MediaObjectDetails mediaObjectDetails = MediaObjectDetails.newBuilder()
                                                        .setMediaId(savedMediaObject.getMediaId())
                                                        .setFileSize(savedMediaObject.getFileSize())
                                                        .setAccessUrl(savedMediaObject.getAccessUrl())
                                                        .setVisibility(getFileVisibility(savedFileVisibility))
                                                        .build();

                                                return MediaUploadResponse.newBuilder()
                                                        .setStatus(OperationStatus.SUCCESS)
                                                        .setMediaObjectDetails(mediaObjectDetails)
                                                        .build();
                                            });
                                        }
                                ))
                ).onErrorResume(err -> Mono.just(MediaUploadResponse.newBuilder()
                        .setStatus(OperationStatus.ERROR)
                        .setErrorMessage(err.getMessage())
                        .build()));
    }


}
