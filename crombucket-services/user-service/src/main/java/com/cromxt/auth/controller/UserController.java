package com.cromxt.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cromxt.auth.dtos.responses.UserResponse;
import com.cromxt.auth.service.UserService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    public Mono<ResponseEntity<UserResponse>> getUser(Authentication authentication){
        String userId = (String) authentication.getPrincipal();
        return userService
        .getUserById(userId)
        .map(userResponse->{
            return ResponseEntity.ok(userResponse);
        });
    }
}
