package com.cromxt.auth.service;

import com.cromxt.auth.dtos.responses.UserResponse;
import com.cromxt.auth.requests.UserRequest;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<Void> saveUser(UserRequest userDetailsDTO);

    Mono<UserResponse> getUserById(String username);
}
