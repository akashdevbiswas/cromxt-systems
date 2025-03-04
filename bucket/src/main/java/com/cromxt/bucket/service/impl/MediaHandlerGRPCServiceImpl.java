package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.client.BucketServerClient;
import com.cromxt.bucket.repository.MediaManager;
import com.cromxt.bucket.service.AccessURLGenerator;
import com.cromxt.bucket.service.FileService;
import com.cromxt.bucket.service.GRPCMediaService;
import com.cromxt.common.crombucket.grpc.MediaHeadersKey;
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
public class MediaHandlerGRPCServiceImpl extends GRPCMediaService {

    private final FileService fileService;
    private final MediaManager mediaManager;
    private final BucketServerClient bucketServerClient;

    //    Handle the upload request in reactive way(Using reactive types Mono and Flux.)
    @Override
    public Mono<MediaUploadResponse> uploadFile(Flux<MediaUploadRequest> request) {


        MediaDetails mediaDetails = MediaHeadersKey.MEDIA_DETAILS.getContextKey().get(Context.current());

        String clientId = mediaDetails.getClientId();

        return bucketServerClient.getBucketInfoByClientId(clientId)
                .flatMap(bucketInfo -> fileService.saveFile(
                                mediaDetails.getExtension(),
                                bucketInfo.getAvailableSpace(),
                                request,
                                !mediaDetails.getIsFilePrivate())
                        .flatMap(fileObjects -> mediaManager
                                .createMediaObject(fileObjects).map(super::createNewSuccessMediaResponse))
                );
    }


}
