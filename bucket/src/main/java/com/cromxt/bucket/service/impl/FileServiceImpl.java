package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.exception.InvalidMediaData;
import com.cromxt.bucket.exception.MediaOperationException;
import com.cromxt.bucket.models.MediaObjects;
import com.cromxt.bucket.service.AccessURLGenerator;
import com.cromxt.bucket.service.FileService;
import com.cromxt.proto.files.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.print.attribute.standard.Media;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Service
@Getter
public class FileServiceImpl implements FileService {

    //    This hashmap is a local database of media objects which are saved in the system.
    private static final Map<String, MediaObjects> MEDIA_OBJECTS_EXISTS = new HashMap<>();

    private final BucketInformationService bucketInformationService;
    private final AccessURLGenerator accessURLGenerator;

    public FileServiceImpl(BucketInformationService bucketInformationService, AccessURLGenerator accessURLGenerator) {
        this.bucketInformationService = bucketInformationService;
        this.accessURLGenerator = accessURLGenerator;

        Flux<MediaObjects> mediaObjects = getAllMediasAvailableOnTheSystem();

        mediaObjects.doOnNext(eachMedia -> {
            MEDIA_OBJECTS_EXISTS.put(eachMedia.getFileId(), eachMedia);
        }).subscribe();
    }


    @Override
    public Flux<MediaObjects> getAllAvailableMedias() {
        return Flux.fromIterable(MEDIA_OBJECTS_EXISTS.values());
    }

    @Override
    public Mono<MediaObjects> saveFile(String extension, Long spaceUserLeft, Flux<MediaUploadRequest> mediaData) {

        return Mono.create(sink -> {
            String uniqueFileName = createAUniqueFileName(extension);
            String absolutePath = String.format("%s/%s.%s", bucketInformationService.getBasePath(), uniqueFileName, extension);

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
                            accessURLGenerator.generateAccessURL(uniqueFileName, bucketInformationService.getBucketId())
                                    .doOnNext(url -> {
                                        System.out.println(url);
                                        MediaObjects newMediaObject = MediaObjects.builder()
                                                .fileId(uniqueFileName)
                                                .fileSize(countSize.get())
                                                .extension(extension)
                                                .accessUrl(url)
                                                .absolutePath(absolutePath)
                                                .build();
                                        sink.success(newMediaObject);
                                        MEDIA_OBJECTS_EXISTS.put(uniqueFileName, newMediaObject);
                                    }).subscribe();
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
    public Mono<MediaObjects> getMediaObjectById(String fileId) {
        return MEDIA_OBJECTS_EXISTS.containsKey(fileId)? Mono.just(MEDIA_OBJECTS_EXISTS.get(fileId)):Mono.error(new InvalidMediaData("Invalid media id"));
    }

    @Override
    public Mono<MediaObjects> deleteMediaObjectById(String mediaId) {
        return Mono.create(sink->{
            if (MEDIA_OBJECTS_EXISTS.containsKey(mediaId)) {
                try {
                    File file = new File(MEDIA_OBJECTS_EXISTS.get(mediaId).getAbsolutePath());
                    boolean result = file.delete();
                    if(result){
                        MediaObjects mediaObjects = MEDIA_OBJECTS_EXISTS.remove(mediaId);
                        sink.success(mediaObjects);
                    }else {
                        sink.error(new MediaOperationException("The file cant delete."));
                    }
                } catch (Exception e) {
                    sink.error(new MediaOperationException(e.getMessage()));
                }
            }
        });
    }

    private String createAUniqueFileName(String extension) {
        String fileName = "";
        while (fileName.isEmpty() || new File(String.format("%s/%s.%s", bucketInformationService.getBasePath(), fileName, extension)).exists())
            fileName = String.format("file-%s", UUID.randomUUID());
        return fileName;
    }

    private Flux<MediaObjects> getAllMediasAvailableOnTheSystem() {
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
            String accessUrl = accessURLGenerator.generateAccessURL(fileNameWithContentType[0], bucketInformationService.getBucketId()).block();
            return MediaObjects.builder()
                    .fileId(fileNameWithContentType[0])
                    .extension(fileNameWithContentType[1])
                    .fileSize(file.length())
                    .accessUrl(accessUrl)
                    .absolutePath(file.getAbsolutePath())
                    .build();
        }).filter(Objects::nonNull).collect(Collectors.toList()));
    }

}
