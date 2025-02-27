package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.exception.MediaOperationException;
import com.cromxt.bucket.service.FileService;
import com.cromxt.proto.files.MediaHeaders;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

@Slf4j
@Service
@Getter
public class FileServiceImpl implements FileService {


    private final String PATH;

    public FileServiceImpl(Environment environment) {
        this.PATH = environment.getProperty("BUCKET_CONFIG_STORAGE_PATH", String.class);
        assert PATH != null;
    }

    @Override
    public FileDetails generateFileDetails(MediaHeaders metaData) throws MediaOperationException {
        String extension = metaData.getContentType();
        String fileId = generateName(extension);
        return FileDetails.builder()
                .fileId(fileId)
                .contentType(metaData.getContentType())
                .absolutePath(String.format("%s/file-%s.%s",PATH, fileId,extension))
                .build();
    }

    @Override
    public String getFileAbsolutePath(String metaData) {
        File file = new File(metaData);
        return String.format("%s/file-%s",PATH,metaData);
    }

    private String generateName(String extension){
        String fileName = UUID.randomUUID().toString();
        while(new File(String.format("%s/%s.%s",PATH,fileName,extension)).exists()) fileName = UUID.randomUUID().toString();
        return fileName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Setter
    @Getter
    @ToString
    public static class FileDetails{
        private String fileId;
        private String contentType;
        private String absolutePath;

    }



}
