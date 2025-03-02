package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.client.MediaManagerClient;
import com.cromxt.bucket.service.FileService;
import com.cromxt.bucket.service.GRPCMediaService;
import com.cromxt.common.crombucket.dtos.mediamanager.requests.UpdateMediaRequestDTO;
import com.cromxt.common.crombucket.grpc.MediaHeadersKey;
import com.cromxt.proto.files.*;
import io.grpc.Context;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
@Slf4j
@Profile({"dev", "prod"})
public class MediaHandlerGRPCServiceImpl extends GRPCMediaService {

    private final FileService fileService;
    private final MediaManagerClient mediaManagerClient;
    private final BucketInformationService bucketInformationService;
    private final DynamicAccessURLGenerator dynamicAccessURLGenerator;

    //    Handle the upload request in reactive way(Using reactive types Mono and Flux.)
    @Override
    public Mono<MediaUploadResponse> uploadFile(Flux<MediaUploadRequest> request) {


        MediaDetails mediaDetails = MediaHeadersKey.MEDIA_DETAILS.getContextKey().get(Context.current());

        String clientId = mediaDetails.getClientId();

        return mediaManagerClient.createMediaObject(clientId).flatMap(newMediaResponse ->
                fileService.saveFile(mediaDetails.getExtension(), newMediaResponse.spaceLeft(), request)
                        .flatMap(mediaObjects -> {
                            UpdateMediaRequestDTO updateMediaRequestDTO = MediaManagerClient.createUpdateMediaRequest(mediaObjects);
                            return mediaManagerClient.updateMediaObject(newMediaResponse.mediaId(), updateMediaRequestDTO)
                                    .map(super::createNewSuccessMediaResponse);
                        }));
    }


}
