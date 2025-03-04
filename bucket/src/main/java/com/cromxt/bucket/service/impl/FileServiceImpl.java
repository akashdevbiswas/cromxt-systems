package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.constants.FileConstants;
import com.cromxt.bucket.exception.FileException;
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
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final BucketInformationService bucketInformationService;


    @Override
    public Mono<FileObjects> saveFile(String extension, Long spaceUserLeft, Flux<MediaUploadRequest> mediaData, Boolean isPublic) {

        return Mono.create(sink -> {

            String fileName = createNewFileName(extension,isPublic);
            String completeFileName = String.format("%s.%s", fileName, extension);
            String absolutePath = getAbsolutePath(completeFileName);


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
                                    .fileId(fileName)
                                    .fileSize(countSize.get())
                                    .extension(extension)
                                    .absolutePath(absolutePath)
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
        String filePath = getAbsolutePath(fileName);
        File file = new File(filePath);
        if (!file.exists()) {
            return Mono.error(new MediaOperationException("File not found with this name " + fileName));
        }

        FileObjects fileObjects = createFileObject(file);

        if (Objects.isNull(fileObjects)) {
            return Mono.error(new MediaOperationException("The requested file is an invalid filename " + fileName));
        }
        return Mono.just(fileObjects);
    }

    @Override
    public Mono<FileObjects> deleteFileByFileName(String fileId) {
        return Mono.create(sink -> {
            String filePath = getAbsolutePath(fileId);
            File file = new File(filePath);

            FileObjects fileObjects = createFileObject(file);

            if (!file.exists()) {
                sink.error(new MediaOperationException("File not found with this name " + fileId));
                return;
            }
            if (file.delete()) {
                sink.error(new MediaOperationException("Error occurred while deleting the file  " + fileId));
                return;
            }
            sink.success(fileObjects);
        });
    }

    @Override
    public Mono<FileObjects> changeFileVisibility(String completeFileName) {
        return Mono.create(sink -> {
            File file = new File(completeFileName);
            String newFileName = changeTheFileAccess(completeFileName);
            String absolutePath = getAbsolutePath(newFileName);

            File changedFile = new File(absolutePath);
            if (file.renameTo(changedFile)) {
                FileObjects fileObject = createFileObject(changedFile);
                sink.success(fileObject);
                return;
            }
            sink.error(new MediaOperationException("Error occurred while changing the file visibility"));
        });
    }

    @NonNull
    private String createNewFileName(String extension, Boolean isPublic) {
        String fileName = createAUniqueFileName(extension);
        if(isPublic)
            return String.format("cm-%s-%s",FileConstants.PRIVATE_ACCESS.getAccessKey(), fileName);
        return String.format("cm-%s-%s",FileConstants.PUBLIC_ACCESS.getAccessKey(), fileName);
    }

    @NonNull
    private String changeTheFileAccess(String completeFileName) {
        Optional<Boolean> filePublic = isFilePublic(completeFileName);
        if(filePublic.isEmpty()) throw new FileException("File is not public or private");

        boolean isPublic = filePublic.get();
        if(isPublic) return String.format("cm-%s-%s",FileConstants.PRIVATE_ACCESS.getAccessKey(), completeFileName);
        return String.format("cm-%s-%s",FileConstants.PUBLIC_ACCESS.getAccessKey(), completeFileName);
    }

    @NonNull
    private String getAbsolutePath(String fileName) {
        return String.format("%s/%s", bucketInformationService.getBaseDirectory(), fileName);
    }

    private String createAUniqueFileName(String extension) {
        String fileName = "";
        while (fileName.isEmpty() || new File(String.format("%s/%s.%s", bucketInformationService.getBaseDirectory(), fileName, extension)).exists())
            fileName = String.format("file-%s", UUID.randomUUID());
        return fileName;
    }


    //    This two services only for local dev purpose where fetch all the files.
    public Flux<FileObjects> getAllAvailableFiles() {
        String basedir = bucketInformationService.getBaseDirectory();

        List<FileObjects> fileObjectsList = new ArrayList<>();

        File baseDirectory = new File(basedir);

        File[] listOfFiles = baseDirectory.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                FileObjects fileObjects = createFileObject(file);
                if (Objects.nonNull(fileObjects)) {
                    fileObjectsList.add(fileObjects);
                }
            }
        }

        return Flux.fromIterable(fileObjectsList);
    }

    private FileObjects createFileObject(File file) {
        String[] fileNameWithExtension = file.getName().split("\\.");

        if (fileNameWithExtension.length != 2) return null;

        Optional<Boolean> isPublic = isFilePublic(file.getName());
        return isPublic.map(aBoolean -> FileObjects.builder()
                .fileId(fileNameWithExtension[0])
                .extension(fileNameWithExtension[1])
                .fileSize(file.length())
                .isPublic(aBoolean)
                .absolutePath(file.getAbsolutePath())
                .build()).orElse(null);

    }

    private Optional<Boolean> isFilePublic(String fileName) {
        String publicAccess = String.format("cm-%s",FileConstants.PUBLIC_ACCESS.getAccessKey());
        String privateAccess = String.format("cm-%s",FileConstants.PRIVATE_ACCESS.getAccessKey());

        if(fileName.startsWith(publicAccess) == fileName.startsWith(privateAccess)){
            return Optional.empty();
        }
        return Optional.of(fileName.startsWith(publicAccess));
    }
}
