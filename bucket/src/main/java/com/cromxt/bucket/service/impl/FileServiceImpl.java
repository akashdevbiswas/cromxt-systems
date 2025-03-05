package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.constants.FileConstants;
import com.cromxt.bucket.exception.MediaOperationException;
import com.cromxt.bucket.models.FileObjects;
import com.cromxt.bucket.service.FileService;
import com.cromxt.proto.files.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final BucketInformationService bucketInformationService;


    @Override
    public Mono<FileObjects> saveFile(String extension,
                                      Long spaceUserLeft,
                                      Flux<MediaUploadRequest> mediaData,
                                      FileConstants visibility
    ) {

        return Mono.create(sink -> {

            String fileName = createAUniqueFileName(extension);
            String completeFileId = addAccessKey(fileName, visibility);
            String absolutePath = getAbsolutePath(completeFileId,visibility);

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
    public Mono<FileObjects> getFileByFileName(String fileName) throws MediaOperationException {
        return Mono.create(sink -> {
            FileDetails fileDetails = validateFileName(fileName);

            if (fileDetails == null) {
                sink.error(new MediaOperationException("The requested file is an invalid filename " + fileName));
                return;
            }
            FileObjects fileObjects = createFileObject(fileDetails);
            sink.success(fileObjects);
        });
    }

    @Override
    public Mono<FileObjects> deleteFileByFileName(String fileName) {
        return Mono.create(sink -> {
            FileDetails fileDetails = validateFileName(fileName);

            if (fileDetails == null) {
                sink.error(new MediaOperationException("The requested file is an invalid filename " + fileName));
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
    public Mono<FileObjects> changeFileVisibility(String completeFileName, FileConstants visibility) {
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
    private String addAccessKey(String fileName, FileConstants visibility) {
        return switch (visibility) {
            case PRIVATE_ACCESS -> String.format("cm-%s-%s", FileConstants.PRIVATE_ACCESS.getAccessType(), fileName);
            case PROTECTED_ACCESS -> String.format("cm-%s-%s", FileConstants.PROTECTED_ACCESS.getAccessType(), fileName);
            case PUBLIC_ACCESS -> String.format("cm-%s-%s", FileConstants.PUBLIC_ACCESS.getAccessType(), fileName);
        };
    }


    private FileDetails changeTheFileAccess(File sourceFile, FileConstants visibility) {
        String fileNameWithoutAccessKey = extractFileName(sourceFile.getName());

        if (fileNameWithoutAccessKey == null) {
            return null;
        }
        String newPath = getAbsolutePath(fileNameWithoutAccessKey,visibility);

        Path source = Paths.get(sourceFile.getAbsolutePath());
        Path destination = Paths.get(newPath);
        try {
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Error occurred while moving the file with message: {}", e.getMessage());
            return null;
        }
        return new FileDetails(new File(newPath), visibility);
    }

    private String extractFileName(String fileName) {
        if (fileName.length() < 14) return null;
        return fileName.substring(13);
    }

    @NonNull
    private String getAbsolutePath(String fileName, FileConstants visibility) {
        String baseDir = bucketInformationService.getBaseDirectory();
        return switch (visibility) {
            case PUBLIC_ACCESS ->
                    String.format("%s/%s/%s", baseDir, FileConstants.PUBLIC_ACCESS.getAccessType(), fileName);
            case PRIVATE_ACCESS ->
                    String.format("%s/%s/%s", baseDir, FileConstants.PRIVATE_ACCESS.getAccessType(), fileName);
            case PROTECTED_ACCESS ->
                    String.format("%s/%s/%s", baseDir, FileConstants.PROTECTED_ACCESS.getAccessType(), fileName);
        };
    }

    private String createAUniqueFileName(String extension) {
        return String.format("file-%s-%s.%s", System.currentTimeMillis(), UUID.randomUUID(), extension);
    }


    //    This two services only for local dev purpose where fetch all the files.
    public Flux<FileObjects> getAllAvailableFiles() {
        String basedir = bucketInformationService.getBaseDirectory();

        List<FileObjects> fileObjectsList = new ArrayList<>();

        File publicDirectory = new File(String.format("%s/%s", basedir, FileConstants.PUBLIC_ACCESS.getAccessType()));

        File[] listOfFiles = publicDirectory.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                FileDetails fileDetails = validateFileName(file.getName());
                if (fileDetails != null) {
                    FileObjects fileObjects = createFileObject(fileDetails);
                    fileObjectsList.add(fileObjects);
                }
            }
        }

        File securedDirectory = new File(String.format("%s/%s", basedir, FileConstants.PRIVATE_ACCESS.getAccessType()));

        File[] listOfSecuredFiles = securedDirectory.listFiles();

        if (listOfSecuredFiles != null) {
            for (File file : listOfSecuredFiles) {
                FileDetails fileDetails = validateFileName(file.getName());
                if (fileDetails != null) {
                    FileObjects fileObjects = createFileObject(fileDetails);
                    fileObjectsList.add(fileObjects);
                }
            }
        }

        return Flux.fromIterable(fileObjectsList);
    }

    FileObjects createFileObject(FileDetails fileDetails){
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
        FileConstants fileVisibility = FileService.getFileVisibility(fileId);
        String path = getAbsolutePath(fileId,fileVisibility);
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
        FileConstants visibility;
    }
}
