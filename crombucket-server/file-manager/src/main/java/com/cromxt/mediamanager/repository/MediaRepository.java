package com.cromxt.mediamanager.repository;

import com.cromxt.mediamanager.entity.Medias;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MediaRepository extends ReactiveMongoRepository<Medias, String> {
    Mono<Medias> findByMediaName(String mediaName);
}
