package com.cromxt.bucket.service;


import com.cromxt.bucket.constants.FileVisibility;
import com.cromxt.bucket.exception.FileException;
import com.cromxt.bucket.models.FileObjects;
import com.cromxt.proto.files.FileUploadRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileService {


    Mono<FileObjects> saveFile(String clientId, String extension, Long spaceUserLeft, Flux<FileUploadRequest> data, FileVisibility visibility);

    Mono<FileObjects> getFileByFileName(String fileId);

    Mono<FileObjects> deleteFileByFileName(String fileId);

    Mono<FileObjects> changeFileVisibility(String fileId, FileVisibility visibility);

    static FileVisibility getFileVisibility(String fileId) {
        String[] splitedString = fileId.split("-");
        if(splitedString.length < 2) throw new FileException("Invalid file name");
        String accessKey = splitedString[1];
        return switch (accessKey) {
            case "pub" -> FileVisibility.PUBLIC_ACCESS;
            case "prv" -> FileVisibility.PRIVATE_ACCESS;
            case "prt" -> FileVisibility.PROTECTED_ACCESS;
            default -> throw new FileException("Filename without invalid key.");
        };
    }

    static String extractFileExtension(String fileId){
        int index = fileId.lastIndexOf(".");
        return fileId.substring(index);
    }

}

