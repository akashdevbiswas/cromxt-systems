package com.cromxt.bucket.service;


import com.cromxt.bucket.models.FileObjects;
import com.cromxt.proto.files.MediaUploadRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileService {


    Mono<FileObjects> saveFile(String extension, Long spaceUserLeft, Flux<MediaUploadRequest> data,Boolean isPublic);

    Mono<FileObjects> getFileByFileName(String fileName);

    Mono<FileObjects> deleteFileByFileName(String fileName);

    Mono<FileObjects> changeFileVisibility(String fileId, Boolean visibility);

}

