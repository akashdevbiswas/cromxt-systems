package com.cromxt.storageserver.service;


import com.cromxt.storageserver.constants.FileVisibility;
import com.cromxt.storageserver.exception.FileException;
import com.cromxt.storageserver.models.FileObjects;
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
            case "pub" -> FileVisibility.PUBLIC;
            case "prv" -> FileVisibility.PRIVATE;
            case "prt" -> FileVisibility.PROTECTED;
            default -> throw new FileException("Filename without invalid key.");
        };
    }

    static String extractFileExtension(String fileId){
        int index = fileId.lastIndexOf(".");
        return fileId.substring(index);
    }

}

