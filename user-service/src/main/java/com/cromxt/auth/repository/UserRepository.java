package com.cromxt.auth.repository;

import com.cromxt.auth.entity.UserEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<UserEntity,String> {

    Mono<UserEntity> findByUsernameOrEmail(String username,String email);
}
