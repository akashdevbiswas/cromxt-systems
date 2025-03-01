package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.models.MediaObjects;
import com.cromxt.bucket.service.AccessURLGenerator;
import com.cromxt.bucket.service.FileService;
import com.cromxt.bucket.service.GRPCMediaService;
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
@Profile("local")
@RequiredArgsConstructor
@Slf4j
public class LocalMediaHandlerGRPCServiceImpl extends GRPCMediaService {


    private final FileService fileService;
    private final AccessURLGenerator accessURLGenerator;
    private final BucketInformationService bucketInformationService;


    @Override
    public Mono<MediaUploadResponse> uploadFile(Flux<MediaUploadRequest> request) {

        MediaDetails mediaDetails = MediaHeadersKey.MEDIA_DETAILS.getContextKey().get(Context.current());

        Long availableSpace = bucketInformationService.getAvailableSpace();

        Mono<MediaObjects> savedMedia = fileService.saveFile(mediaDetails.getExtension(), availableSpace, request);
        return savedMedia.map(super::createNewSuccessMediaResponse);
    }

}
