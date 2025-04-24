package com.crombucket.mediaservice.repository;

import com.crombucket.mediaservice.entity.Medias;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MediaRepository extends ReactiveMongoRepository<Medias, String> {

}
