package com.cromxt.bucket.repository.impl;


import com.cromxt.bucket.models.FileObjects;
import com.cromxt.bucket.models.MediaObjects;
import com.cromxt.bucket.repository.MediaManager;
import com.cromxt.bucket.repository.MediaRepository;
import com.cromxt.bucket.service.AccessURLGenerator;
import com.cromxt.bucket.service.impl.BucketInformationService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Profile("local")
public class InMemoryMediaManager extends MediaManager implements MediaRepository {

    //    This hashmap is a local database of media objects which are saved in the system.
    private static final Map<String, MediaObjects> MEDIA_OBJECTS_EXISTS = new HashMap<>();
    private final BucketInformationService bucketInformationService;

    public InMemoryMediaManager(AccessURLGenerator accessURLGenerator, BucketInformationService bucketInformationService) {
        super(accessURLGenerator);
        this.bucketInformationService = bucketInformationService;
    }

    @Override
    public Mono<MediaObjects> createMediaObject(FileObjects fileObjects) {
        String uniqueId = "";

        while (uniqueId.isEmpty() || MEDIA_OBJECTS_EXISTS.containsKey(uniqueId)) {
            uniqueId = UUID.randomUUID().toString();
        }

        MediaObjects mediaObjects = creteFileObject(fileObjects);
        MEDIA_OBJECTS_EXISTS.put(uniqueId, mediaObjects);
        return Mono.just(mediaObjects);
    }

    @Override
    public Mono<Void> deleteMedia(String mediaId) {
        return Mono.fromRunnable(() -> MEDIA_OBJECTS_EXISTS.remove(mediaId)).then();
    }

    @Override
    public Mono<MediaObjects> updateMedia(String mediaObjectId, FileObjects fileObjects) {
        return Mono.create(sink -> {
            MEDIA_OBJECTS_EXISTS.remove(mediaObjectId);
            MediaObjects mediaObjects = creteFileObject(fileObjects);
            String fileId = mediaObjects.getFileId();
            MEDIA_OBJECTS_EXISTS.put(fileId, mediaObjects);
            sink.success(mediaObjects);
        });
    }


    @Override
    public Flux<MediaObjects> getAllAvailableMedias() {
        return Flux.fromIterable(MEDIA_OBJECTS_EXISTS.values());
    }

    @Override
    public Mono<MediaObjects> getMediaObjectById(String mediaId) {
        return Mono.just(MEDIA_OBJECTS_EXISTS.get(mediaId));
    }

    private MediaObjects creteFileObject(FileObjects fileObjects) {
        String accessUrl = accessURLGenerator.generateAccessURL(fileObjects.getFileId());
        return MediaObjects.builder()
                .mediaId(fileObjects.getFileId())
                .fileId(fileObjects.getFileId())
                .accessUrl(accessUrl)
                .extension(fileObjects.getExtension())
                .fileSize(fileObjects.getFileSize())
                .visibility(fileObjects.getVisibility().name())
                .build();
    }
}
