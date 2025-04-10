package com.cromxt.storageservice.service.impl;

import com.cromxt.storageservice.constants.FileVisibility;
import com.cromxt.storageservice.models.FileObjects;
import com.cromxt.storageservice.service.AccessURLGenerator;
import com.cromxt.storageservice.service.FileService;
import com.cromxt.storageservice.service.GRPCService;
import com.cromxt.proto.files.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class FileManagementGRPCService extends ReactorFileManagementServiceGrpc.FileManagementServiceImplBase implements GRPCService {

    private final FileService fileService;
    private final AccessURLGenerator accessURLGenerator;

    @Override
    public Mono<FileOperationResponse> changeFileVisibility(Mono<UpdateVisibilityRequest> request) {

        return request.flatMap(updateVisibilityRequest -> {
            FileVisibility visibility = this.getFileVisibility(updateVisibilityRequest.getVisibility());
            Mono<FileObjects> deletedFileObject = fileService.changeFileVisibility(updateVisibilityRequest.getFileId(), visibility);
            return deletedFileObject.map(fileObjects -> {
                String fileId = fileObjects.getFileId();

                String accessUrl = accessURLGenerator.generateAccessURL(fileId);

                FileObjectDetails fileObjectDetails = FileObjectDetails.newBuilder()
                        .setFileId(fileObjects.getFileId())
                        .setAccessUrl(accessUrl)
                        .setFileSize(fileObjects.getFileSize())
                        .setExtension(fileObjects.getExtension())
                        .setVisibility(updateVisibilityRequest.getVisibility())
                        .build();

                return FileOperationResponse.newBuilder()
                        .setStatus(OperationStatus.SUCCESS)
                        .setFileObjectDetails(fileObjectDetails)
                        .build();
            });
        }).onErrorResume(err -> Mono.just(FileOperationResponse.newBuilder()
                .setStatus(OperationStatus.ERROR)
                .setResponseMessage(err.getMessage())
                .build()));
    }

    @Override
    public Mono<FileOperationResponse> deleteFile(Mono<FileObject> request) {
        return request.flatMap(fileObject -> {
                    Mono<FileObjects> deletedFileObject = fileService.deleteFileByFileName(fileObject.getFileId());
                    return deletedFileObject.map(fileObjects -> FileOperationResponse.newBuilder()
                            .setResponseMessage("File deleted successfully")
                            .setStatus(OperationStatus.SUCCESS)
                            .build());
                })
                .onErrorResume(err -> Mono.just(FileOperationResponse.newBuilder()
                        .setStatus(OperationStatus.ERROR)
                        .setResponseMessage(err.getMessage())
                        .build()));
    }
}
