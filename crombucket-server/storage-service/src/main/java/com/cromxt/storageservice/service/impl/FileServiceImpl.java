package com.cromxt.storageservice.service.impl;

import com.cromxt.storageservice.constants.FileVisibility;
import com.cromxt.storageservice.exception.MediaOperationException;
import com.cromxt.storageservice.models.FileObjects;
import com.cromxt.storageservice.service.FileService;
import com.cromxt.proto.files.FileUploadRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@Getter
public class FileServiceImpl implements FileService {

    private final StorageServerDetails storageServerDetails;


    public FileServiceImpl(StorageServerDetails storageServerDetails, ApplicationContext applicationContext) {
        this.storageServerDetails = storageServerDetails;

        String basePath = storageServerDetails.getBaseDirectory();
        assert basePath != null;

        File directory = new File(basePath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                log.error("Unable to create the public directory");
                SpringApplication.exit(applicationContext, () -> 1);
            }
        }
    }

    @Override
    public Mono<FileObjects> saveFile(String clientId,
                                      String extension,
                                      Long spaceUserLeft,
                                      Flux<FileUploadRequest> mediaData,
                                      FileVisibility visibility
    ) {

        return Mono.create(sink -> {

            String fileName = createAUniqueFileName(clientId, extension);
            String completeFileId = addAccessKey(fileName, visibility);
            String absolutePath = getAbsolutePath(completeFileId);

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(absolutePath);

                AtomicLong countSize = new AtomicLong(0L);
                mediaData.subscribeOn(Schedulers.boundedElastic())
                        .doOnNext(chunkData -> {
                            byte[] data = chunkData.getFile().toByteArray();
//                            Increment the count size before write, and if the countSize is greater than the availableSpace then break.
                            countSize.addAndGet(data.length);
                            if (countSize.get() > spaceUserLeft) {
                                throw new MediaOperationException("Media data size is greater than the size user left of.");
                            }
                            try {
                                fileOutputStream.write(data);
                            } catch (Exception e) {
                                log.error("Error occurred while write the data on the disk with message: {}", e.getMessage());
                                throw new MediaOperationException("Encounter an error while write the data on the disk with message: " + e.getMessage());
                            }
                        })
                        .doOnComplete(() -> {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e) {
                                log.error("Error occurred while closing the FileOutputStream with message: {}", e.getMessage());
                                throw new MediaOperationException(e.getMessage());
                            }
                            FileObjects newMediaObject = FileObjects.builder()
                                    .fileId(completeFileId)
                                    .fileSize(countSize.get())
                                    .extension(extension)
                                    .absolutePath(absolutePath)
                                    .visibility(visibility)
                                    .build();
                            sink.success(newMediaObject);
                        })
                        .doOnError(e -> {
                            log.error("Error occurred while saving the file with message: {}", e.getMessage());
                            sink.error(new MediaOperationException(e.getMessage()));
                        }).subscribe();
            } catch (IOException e) {
                log.error("Error occurred while creating FileOutputStream with message: {}", e.getMessage());
                sink.error(new MediaOperationException(e.getMessage()));
            }
        });
    }

    @Override
    public Mono<FileObjects> getFileByFileName(String fileId) throws MediaOperationException {
        return Mono.create(sink -> {
            FileDetails fileDetails = validateFileName(fileId);

            if (fileDetails == null) {
                sink.error(new MediaOperationException("The requested file is an invalid filename " + fileId));
                return;
            }
            FileObjects fileObjects = createFileObject(fileDetails);
            sink.success(fileObjects);
        });
    }

    @Override
    public Mono<FileObjects> deleteFileByFileName(String fileId) {
        return Mono.create(sink -> {
            FileDetails fileDetails = validateFileName(fileId);

            if (fileDetails == null) {
                sink.error(new MediaOperationException("The requested file is an invalid filename " + fileId));
                return;
            }
            File file = fileDetails.getFile();
            if (file.delete()) {
                FileObjects fileObjects = createFileObject(fileDetails);
                sink.success(fileObjects);
                return;
            }
            sink.error(new MediaOperationException("Error occurred while deleting the file"));
        });
    }


    @Override
    public Mono<FileObjects> changeFileVisibility(String completeFileName, FileVisibility visibility) {
        return Mono.create(sink -> {
            FileDetails details = validateFileName(completeFileName);
            if (details == null) {
                sink.error(new MediaOperationException("The requested file is an invalid filename " + completeFileName));
                return;
            }
            if (details.getVisibility() == visibility) {
                FileObjects fileObjects = createFileObject(details);
                sink.success(fileObjects);
                return;
            }

            FileDetails fileDetails = changeTheFileAccess(details.getFile(), visibility);

            if (fileDetails == null) {
                sink.error(new MediaOperationException("Error occurred while changing the file visibility"));
                return;
            }
            FileObjects fileObjects = createFileObject(fileDetails);
            sink.success(fileObjects);

        });
    }


    @NonNull
    private String addAccessKey(String fileName, FileVisibility visibility) {
        return switch (visibility) {
            case PRIVATE -> String.format("cm-%s-%s", FileVisibility.PRIVATE.getAccessType(), fileName);
            case PROTECTED -> String.format("cm-%s-%s", FileVisibility.PROTECTED.getAccessType(), fileName);
            case PUBLIC -> String.format("cm-%s-%s", FileVisibility.PUBLIC.getAccessType(), fileName);
        };
    }


    private FileDetails changeTheFileAccess(File sourceFile, FileVisibility visibility) {
        String fileNameWithoutAccessKey = extractFileName(sourceFile.getName());

        if (fileNameWithoutAccessKey == null) {
            return null;
        }

        String newFileName = addAccessKey(fileNameWithoutAccessKey, visibility);

        String absolutePath = getAbsolutePath(newFileName);

        File newFile = new File(absolutePath);

        if (!sourceFile.renameTo(newFile)) {
            return null;
        }

        return new FileDetails(newFile, visibility);
    }

    private String extractFileName(String fileName) {
        int len = FileVisibility.PUBLIC.getAccessType().length() + 4;
        if (fileName.length() < len) return null;
        return fileName.substring(len);
    }

    @NonNull
    private String getAbsolutePath(String fileId) {
        return String.format("%s/%s", storageServerDetails.getBaseDirectory(), fileId);
    }

    private String createAUniqueFileName(String clientId, String extension) {

        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            String input = clientId + System.currentTimeMillis();
            byte[] hashBytes = digest.digest(input.getBytes());

            for (byte b : hashBytes) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }

        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        return String.format("file-%s.%s", hexString, extension);
    }


    //    This two services only for local dev purpose where fetch all the files.
    public Flux<FileObjects> getAllAvailableFiles() {
        String basedir = storageServerDetails.getBaseDirectory();

        List<FileObjects> fileObjectsList = new ArrayList<>();

        File currentDirectory = new File(basedir);

        if(!currentDirectory.isDirectory() || !currentDirectory.exists()) {
            throw new MediaOperationException("No Base directory found.");
        }

        File[] listOfSecuredFiles = currentDirectory.listFiles();
        if(listOfSecuredFiles == null) {
            return Flux.fromIterable(fileObjectsList);
        }
        for (File file : listOfSecuredFiles) {
            FileDetails fileDetails = validateFileName(file.getName());
            if (fileDetails != null) {
                FileObjects fileObjects = createFileObject(fileDetails);
                fileObjectsList.add(fileObjects);
            }
        }
        return Flux.fromIterable(fileObjectsList);
    }

    FileObjects createFileObject(FileDetails fileDetails) {
        File file = fileDetails.getFile();
        String extension = FileService.extractFileExtension(file.getName());
        return FileObjects.builder()
                .fileId(file.getName())
                .fileSize(file.length())
                .extension(extension)
                .absolutePath(file.getAbsolutePath())
                .visibility(fileDetails.getVisibility())
                .build();
    }

    private FileDetails validateFileName(String fileId) {
        if (!fileId.startsWith("cm-")) return null;
        FileVisibility fileVisibility = FileService.getFileVisibility(fileId);
        String path = getAbsolutePath(fileId);
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        return new FileDetails(file, fileVisibility);
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private static class FileDetails {
        File file;
        FileVisibility visibility;
    }
}
