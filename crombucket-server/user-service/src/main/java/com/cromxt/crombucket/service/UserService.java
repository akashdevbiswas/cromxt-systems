package com.cromxt.crombucket.service;

import com.cromxt.crombucket.dtos.requests.UserDetailsDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<Void> saveUser(UserDetailsDTO userDetailsDTO);
}
