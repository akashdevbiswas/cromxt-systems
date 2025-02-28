package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.exception.MediaOperationException;
import com.cromxt.bucket.models.MediaObjects;
import com.cromxt.bucket.service.AccessURLGenerator;
import com.cromxt.bucket.service.FileService;
import com.cromxt.proto.files.MediaDetails;
import com.cromxt.proto.files.MediaHeaders;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {


    private final BucketInformationService bucketInformationService;
    private final AccessURLGenerator accessURLGenerator;

    @Override
    public FileDetails generateFileDetails(String contentType) throws MediaOperationException {
        String fileId = generateName(contentType);
        return FileDetails.builder()
                .fileId(fileId)
                .contentType(contentType)
                .absolutePath(String.format("%s/file-%s.%s", bucketInformationService.getBasePath(), fileId, contentType))
                .build();
    }

    @Override
    public String getFileAbsolutePath(String mediaId) {
        return String.format("%s/file-%s", bucketInformationService.getBasePath(), mediaId);
    }

    @Override
    public Flux<MediaObjects> getAllAvailableMedias() {
        File rootDirectory = new File(bucketInformationService.getBasePath());

        File[] listOfFiles = rootDirectory.listFiles();
        if (listOfFiles == null) {
            return Flux.empty();
        }
        return Flux.fromIterable(Arrays.stream(listOfFiles).map(file -> {
            String[] fileNameWithContentType = file.getName().split("\\.");
            if (fileNameWithContentType.length != 2) {
                return null;
            }
            String accessUrl = accessURLGenerator.generateAccessURL(fileNameWithContentType[0]).block();
            return MediaObjects.builder()
                    .mediaId(fileNameWithContentType[0])
                    .contentType(fileNameWithContentType[1])
                    .fileSize(file.length())
                    .accessUrl(accessUrl)
                    .build();
        }).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    private String generateName(String extension) {
        String fileName = UUID.randomUUID().toString();
        while (new File(String.format("%s/file-%s.%s", bucketInformationService.getBasePath(), fileName, extension)).exists())
            fileName = UUID.randomUUID().toString();
        return fileName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Setter
    @Getter
    @ToString
    public static class FileDetails {
        private String fileId;
        private String contentType;
        private String absolutePath;

    }

}
