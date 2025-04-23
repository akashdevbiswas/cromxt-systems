package com.cromxt.jwt.service;

import com.cromxt.jwt.dtos.requests.UserDetailsDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<Void> saveUser(UserDetailsDTO userDetailsDTO);
}
