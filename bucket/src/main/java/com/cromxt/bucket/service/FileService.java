package com.cromxt.bucket.service;


import com.cromxt.bucket.constants.FileConstants;
import com.cromxt.bucket.exception.FileException;
import com.cromxt.bucket.exception.MediaOperationException;
import com.cromxt.bucket.models.FileObjects;
import com.cromxt.bucket.service.impl.FileServiceImpl;
import com.cromxt.proto.files.MediaUploadRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileService {


    Mono<FileObjects> saveFile(String extension, Long spaceUserLeft, Flux<MediaUploadRequest> data,FileConstants visibility);

    Mono<FileObjects> getFileByFileName(String fileName);

    Mono<FileObjects> deleteFileByFileName(String fileName);

    Mono<FileObjects> changeFileVisibility(String fileId, FileConstants visibility);

    static FileConstants getFileVisibility(String fileId) {
        String[] splitedString = fileId.split("-");
        if(splitedString.length < 2) throw new FileException("Invalid file name");
        String accessKey = splitedString[1];
        return switch (accessKey) {
            case "pub" -> FileConstants.PUBLIC_ACCESS;
            case "prv" -> FileConstants.PRIVATE_ACCESS;
            case "prt" -> FileConstants.PROTECTED_ACCESS;
            default -> throw new FileException("Filename without invalid key.");
        };
    }

    static String extractFileExtension(String fileId){
        int index = fileId.lastIndexOf(".");
        return fileId.substring(index);
    }

}

