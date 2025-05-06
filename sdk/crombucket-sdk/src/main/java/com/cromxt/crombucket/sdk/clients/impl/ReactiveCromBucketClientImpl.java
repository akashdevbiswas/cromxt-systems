package com.cromxt.crombucket.sdk.clients.impl;


import com.crombucket.common.mediaservice.response.MediaObjects;
import com.crombucket.common.routeing.MediaDetails;
import com.crombucket.common.routeing.StorageServerAddress;
import com.cromxt.crombucket.sdk.BucketUserDetails;
import com.crombucket.proto.storagenode.*;
import com.cromxt.crombucket.sdk.FileVisibility;
import com.cromxt.crombucket.sdk.UpdateFileVisibilityRequest;
import com.cromxt.crombucket.sdk.clients.ReactiveCromBucketClient;
import com.cromxt.crombucket.sdk.exceptions.CromBucketServerException;
import com.cromxt.crombucket.sdk.response.FileResponse;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ReactiveCromBucketClientImpl extends ReactiveCromBucketClient {

    private final BucketUserDetails clientCredentials;
    private final WebClient webClient;


    @Override
    public Mono<FileResponse> saveFile(FilePart file, Long fileSize) {
        return initiateUploading(FileVisibility.PUBLIC, fileSize, extractExtension(file.filename()), file.content());
    }

    @Override
    public Mono<FileResponse> saveFile(FilePart file, Long fileSize, FileVisibility visibility) {
        return initiateUploading(visibility, fileSize, extractExtension(file.filename()), file.content());
    }

    @Override
    public Mono<Void> delete(String mediaId) {
        return deleteMedias(List.of(mediaId));
    }

    @Override
    public Mono<Void> deleteMany(List<String> mediaIds) {
        return deleteMedias(mediaIds);
    }

    @Override
    public Mono<FileResponse> changeFileVisibility(UpdateFileVisibilityRequest updateFileVisibilityRequest) {
        return updateVisibility(updateFileVisibilityRequest);
    }

    @Override
    public Flux<FileResponse> changeFileVisibility(List<UpdateFileVisibilityRequest> updateFileVisibilityRequest) {
        return null;
    }


    @Override
    public Mono<FileResponse> updateFile(String mediaId, FilePart filePart, Long fileSize) {
        return initiateUpdate(mediaId, filePart, fileSize, FileVisibility.PUBLIC);
    }

    @Override
    public Mono<FileResponse> updateFile(String mediaId, FilePart filePart, Long fileSize, FileVisibility visibility) {
        return initiateUpdate(mediaId, filePart, fileSize, visibility);
    }


    private Mono<FileResponse> initiateUpdate(String mediaId, FilePart filePart, Long fileSize, FileVisibility visibility) {
        Mono<Void> deletedFile = deleteMedias(List.of(mediaId));
        String extension = extractExtension(filePart.filename());
        return deletedFile.flatMap(ignored -> initiateUploading(visibility, fileSize, extension, filePart.content()));
    }

    private Mono<FileResponse> initiateUploading(
            FileVisibility visibility,
            Long fileSize,
            String extension,
            Flux<DataBuffer> fileData
    ) {
        Mono<StorageServerAddress> bucketDetails = fetchStorageServer(fileSize);

        Metadata metadata = generateHeaders(extension, clientCredentials.getClientSecret(), visibility);

        return bucketDetails
                .flatMap(bucket ->
                        uploadFile(fileData, metadata, bucket).map(mediaObjectDetails -> FileResponse.builder()
                                .mediaId(mediaObjectDetails.getMediaId())
                                .fileSize(mediaObjectDetails.getFileSize())
                                .accessUrl(mediaObjectDetails.getAccessUrl())
                                .visibility(getVisibility(mediaObjectDetails.getVisibility()))
                                .build()
                        ));
    }

    private Mono<StorageServerAddress> fetchStorageServer(Long fileSize) {
//       TODO: Add all media details;
        String url = String.format("%s/%s", clientCredentials.getBaseUrl(), BUCKET_MANAGER_URL);

        MediaDetails mediaDetails = MediaDetails.builder()
                .fileSize(fileSize)
                .build();

        return webClient
                .post()
                .uri(URI.create(url))
                .header("Api-Key", clientCredentials.getClientSecret())
                .bodyValue(mediaDetails)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (clientResponse -> Mono.error(new CromBucketServerException("Some error occurred."))))
                .bodyToMono(StorageServerAddress.class);
    }

    private Mono<FileResponse> updateVisibility(UpdateFileVisibilityRequest updateFileVisibilityRequests){
        String url = String.format("%s/%s",clientCredentials.getBaseUrl(),MEDIA_MANAGER_URL);
        return webClient
                .put()
                .uri(URI.create(url))
                .bodyValue(updateFileVisibilityRequests)
                .header("Authorization", clientCredentials.getClientSecret())
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new CromBucketServerException("Some error occurred.")))
                .bodyToMono(MediaObjects.class)
                .map(mediaObjects -> FileResponse.builder()
                        .accessUrl(mediaObjects.getAccessUrl())
                        .visibility(FileVisibility.valueOf(mediaObjects.getVisibility()))
                        .fileSize(mediaObjects.getFileSize())
                        .mediaId(mediaObjects.getMediaId())
                        .build()
                );
    }


    private Mono<MediaObjectDetails> uploadFile(Flux<DataBuffer> fileData,
                                                Metadata metadata,
                                                StorageServerAddress storageServerAddress) {

        ManagedChannel channel = createNettyManagedChannel(storageServerAddress);

        // Use of reactive implementation instead of blocking.

        ReactorFileHandlerServiceGrpc.ReactorFileHandlerServiceStub reactorFileHandlerServiceStub = ReactorFileHandlerServiceGrpc
                .newReactorStub(channel)
                .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata));

        Flux<FileUploadRequest> data = fileData
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    DataBufferUtils.release(dataBuffer);
                    return FileUploadRequest
                            .newBuilder()
                            .setFile(ByteString.copyFrom(bytes))
                            .build();
                });

        return reactorFileHandlerServiceStub
                .uploadFile(data)
                .doOnError(err -> MediaUploadResponse.newBuilder().setStatus(OperationStatus.ERROR).setErrorMessage(err.getMessage()).build())
                .flatMap(mediaUploadResponse -> {
                    channel.shutdown();
                    if (mediaUploadResponse.getStatus() == OperationStatus.ERROR) {
                        return Mono.error(new CromBucketServerException(mediaUploadResponse.getErrorMessage()));
                    }
                    return Mono.just(mediaUploadResponse.getMediaObjectDetails());
                });

    }


    private Mono<Void> deleteMedias(List<String> mediaIds){
        String url = String.format("%s/%s/delete",clientCredentials.getBaseUrl(),MEDIA_MANAGER_URL);
        return webClient
                .post()
                .uri(URI.create(url))
                .header("Authorization", clientCredentials.getClientSecret())
                .bodyValue(mediaIds)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new CromBucketServerException("Some error occurred.")))
                .toBodilessEntity()
                .then();
    }

}
