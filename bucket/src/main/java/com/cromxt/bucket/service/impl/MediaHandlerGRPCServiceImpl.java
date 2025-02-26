package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.client.MediaSeverClient;
import com.cromxt.bucket.exception.InvalidMediaData;
import com.cromxt.bucket.exception.MediaOperationException;
import com.cromxt.bucket.service.FileService;
import com.cromxt.common.crombucket.dtos.mediaserver.requests.MediaStatus;
import com.cromxt.common.crombucket.dtos.mediaserver.requests.NewMediaRequest;
import com.cromxt.common.crombucket.dtos.mediaserver.requests.UpdateMediaRequest;
import com.cromxt.grpc.MediaHeadersKey;
import com.cromxt.proto.files.*;
import io.grpc.Context;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import static com.cromxt.bucket.service.impl.FileServiceImpl.FileDetails;


@Service
@RequiredArgsConstructor
@Slf4j
public class MediaHandlerGRPCServiceImpl extends ReactorMediaHandlerServiceGrpc.MediaHandlerServiceImplBase {

    private final FileService fileService;
    private final MediaSeverClient mediaSeverClient;
    private final BucketInformationService bucketInformationService;

    //    Handle the upload request in reactive way(Using reactive types Mono and Flux.)
    @Override
    public Mono<MediaUploadResponse> uploadFile(Flux<MediaUploadRequest> request) {

        return Mono.create(sink -> {


            MediaHeaders mediaMetaData = MediaHeadersKey.MEDIA_META_DATA.getContextKey().get(Context.current());
            FileDetails fileDetails = fileService.generateFileDetails(mediaMetaData);

            NewMediaRequest mediaDetails = new NewMediaRequest(
                    "user-1",
                    bucketInformationService.getBucketId(),
                    mediaMetaData.getContentType(),
                    fileDetails.getFileId(),
                    MediaStatus.UPLOADING
            );


            mediaSeverClient.createMediaObject(mediaDetails).doOnNext(bucketId -> {
                long actualSize = fileDetails.getFileSize();
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(fileDetails.getAbsolutePath());
                    AtomicLong countSize = new AtomicLong(0L);
                    request.subscribeOn(Schedulers.boundedElastic())
                            .doOnNext(chunkData -> {
                                byte[] data = chunkData.getFile().toByteArray();
                                countSize.addAndGet(data.length);
                                if (countSize.get() > actualSize) {
                                    throw new InvalidMediaData("The mentioned data is greater than the actual size");
                                }
                                try {
                                    fileOutputStream.write(data);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .doOnComplete(() -> {
                                try {
                                    fileOutputStream.close();
                                    UpdateMediaRequest updateMediaDetails = new UpdateMediaRequest(
                                            countSize.get()
                                    );
                                    mediaSeverClient.updateMediaObject(bucketId, updateMediaDetails).doOnSubscribe((ignored) -> {
                                        sink.success(MediaUploadResponse.newBuilder()
                                                .setStatus(OperationStatus.SUCCESS)
                                                .setFileName(fileDetails.getFileId())
                                                .build());
                                    }).doOnError(e -> {
                                        File file = new File(fileDetails.getAbsolutePath());

                                        boolean result = file.delete();
                                        if (!result) {
                                            log.error("File not deleted with file name {} with error {}", fileDetails.getAbsolutePath(), e.getMessage());
                                        }
                                        sink.success(MediaUploadResponse.newBuilder()
                                                .setStatus(OperationStatus.ERROR)
                                                .setErrorMessage(e.getMessage())
                                                .build());
                                    }).subscribe();
                                } catch (IOException e) {
                                    throw new MediaOperationException(e.getMessage());
                                }
                            })
                            .doOnError(e -> {
                                mediaSeverClient.deleteMediaObject(bucketId)
                                        .doOnError(err->{
                                            log.error("Error occurred while deleting the media object from media server {}",err.getMessage());
                                        })
                                        .subscribe();
                                sink.success(MediaUploadResponse.newBuilder()
                                        .setStatus(OperationStatus.ERROR)
                                        .setErrorMessage(e.getMessage())
                                        .build());
                            })
                            .subscribe();
                } catch (IOException e) {
                    sink.success(MediaUploadResponse.newBuilder()
                            .setStatus(OperationStatus.ERROR)
                            .build());
                }

            }).doOnError(e -> {
                sink.success(MediaUploadResponse.newBuilder()
                        .setStatus(OperationStatus.ERROR)
                        .setErrorMessage(e.getMessage())
                        .build());
            }).subscribe();

        });
    }


}
