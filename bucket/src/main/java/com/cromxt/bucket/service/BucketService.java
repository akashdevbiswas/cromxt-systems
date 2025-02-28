package com.cromxt.bucket.service;

import com.cromxt.bucket.models.MediaObjects;
import reactor.core.publisher.Flux;

public interface BucketService {
    Flux<MediaObjects> getAllAvailableMedias();
}
