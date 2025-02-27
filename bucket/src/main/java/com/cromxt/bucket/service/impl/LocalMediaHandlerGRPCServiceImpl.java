package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.exception.MediaOperationException;
import com.cromxt.bucket.models.MediaObjects;
import com.cromxt.bucket.service.AccessURLGenerator;
import com.cromxt.bucket.service.FileService;
import com.cromxt.bucket.service.GRPCMediaService;
import com.cromxt.grpc.MediaHeadersKey;
import com.cromxt.proto.files.MediaHeaders;
import com.cromxt.proto.files.MediaUploadRequest;
import com.cromxt.proto.files.MediaUploadResponse;
import com.cromxt.proto.files.OperationStatus;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


@Service
@Profile("local")
@RequiredArgsConstructor
@Slf4j
public class LocalMediaHandlerGRPCServiceImpl extends GRPCMediaService {


    private final FileService fileService;
    private final AccessURLGenerator accessURLGenerator;
    private static final List<MediaObjects> SAVED_MEDIA_OBJECTS = new ArrayList<>();


    @Override
    public Mono<MediaUploadResponse> uploadFile(Flux<MediaUploadRequest> request) {

        return Mono.create(sink -> {

            MediaHeaders mediaMetaData = MediaHeadersKey.MEDIA_META_DATA.getContextKey().get(Context.current());
            FileServiceImpl.FileDetails fileDetails = fileService.generateFileDetails(mediaMetaData);

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

                            SAVED_MEDIA_OBJECTS.add(
                                    MediaObjects.builder()
                                            .mediaId(fileDetails.getFileId())
                                            .accessUrl(accessURLGenerator.generateAccessURL(fileDetails.getFileId()))
                                            .contentType(fileDetails.getContentType())
                                            .fileSize(countSize.get())
                                            .build()
                            );
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
