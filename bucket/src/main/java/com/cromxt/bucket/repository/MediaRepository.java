package com.cromxt.bucket.repository;

import com.cromxt.bucket.models.FileObjects;
import com.cromxt.bucket.models.MediaObjects;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MediaRepository {

    Flux<MediaObjects> getAllAvailableMedias();

    Mono<MediaObjects> getMediaObjectById(String mediaId);
}
