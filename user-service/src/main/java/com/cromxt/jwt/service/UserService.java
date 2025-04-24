package com.cromxt.jwt.service;

import com.cromxt.jwt.requests.UserDetailsDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<Void> saveUser(UserDetailsDTO userDetailsDTO);
}
