package com.cromxt.mediaservice.repository;

import com.cromxt.mediaservice.entity.Medias;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface MediaRepository extends ReactiveMongoRepository<Medias, String> {

}
