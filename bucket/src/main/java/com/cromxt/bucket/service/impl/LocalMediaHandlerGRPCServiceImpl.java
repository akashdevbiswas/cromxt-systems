package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.exception.MediaOperationException;
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
import reactor.core.scheduler.Schedulers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


@Service
@Profile("local")
@RequiredArgsConstructor
@Slf4j
public class LocalMediaHandlerGRPCServiceImpl extends GRPCMediaService {


    private final FileService fileService;
    private final AccessURLGenerator accessURLGenerator;


    @Override
    public Mono<MediaUploadResponse> uploadFile(Flux<MediaUploadRequest> request) {

        return Mono.create(sink -> {

            MediaDetails mediaDetails = MediaHeadersKey.MEDIA_DETAILS.getContextKey().get(Context.current());
            FileServiceImpl.FileDetails fileDetails = fileService.generateFileDetails(mediaDetails.getContentType());

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(fileDetails.getAbsolutePath());

                AtomicLong countSize = new AtomicLong(0L);
                request.subscribeOn(Schedulers.boundedElastic())
                        .doOnNext(chunkData -> {
                            byte[] data = chunkData.getFile().toByteArray();

                            try {
                                fileOutputStream.write(data);
                                countSize.addAndGet(data.length);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .doOnComplete(() -> {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e) {
                                throw new MediaOperationException(e.getMessage());
                            }

                            String accessURL = accessURLGenerator.generateAccessURL(fileDetails.getFileId()).block();

                            sink.success(MediaUploadResponse.newBuilder()
                                    .setStatus(OperationStatus.SUCCESS)
                                    .setMediaObjectDetails(MediaObjectDetails.newBuilder()
                                            .setFileId(fileDetails.getFileId())
                                            .setAccessUrl(accessURL)
                                            .setContentType(fileDetails.getContentType())
                                            .setCreatedOn(Date.from(Instant.now()).toString())
                                            .setFileSize(countSize.get())
                                            .build())
                                    .build());
                        })
                        .doOnError(e -> {
                            sink.success(MediaUploadResponse.newBuilder()
                                    .setStatus(OperationStatus.ERROR)
                                    .setErrorMessage(e.getMessage())
                                    .build());

                        }).subscribe();
            } catch (IOException e) {
                sink.success(MediaUploadResponse.newBuilder()
                        .setStatus(OperationStatus.ERROR)
                        .setErrorMessage("Some error occurred")
                        .build());
            }

        });

    }

}
