package com.cromxt.auth.service;

import com.cromxt.auth.requests.UserDetailsDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<Void> saveUser(UserDetailsDTO userDetailsDTO);
}
