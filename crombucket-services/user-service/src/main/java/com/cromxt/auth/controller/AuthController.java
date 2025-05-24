package com.cromxt.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cromxt.auth.requests.UserCredentials;
import com.cromxt.auth.requests.UserRequest;
import com.cromxt.auth.responses.AuthTokens;
import com.cromxt.auth.service.AuthService;
import com.cromxt.auth.service.UserService;
import com.cromxt.http.ResponseBuilder;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final ResponseBuilder responseBuilder;

    @PostMapping
    public Mono<ResponseEntity<AuthTokens>> login(@RequestBody UserCredentials userCredentials) {
        Mono<AuthTokens> authTokens = authService.authenticateUserCredentials(userCredentials);
        return responseBuilder.buildResponseWithBody(authTokens, HttpStatus.CREATED);
    }

    @PostMapping(value = "/register")
    public Mono<ResponseEntity<Void>> register(@RequestBody UserRequest userDetailsDTO) {
        Mono<Void> emptyMono = userService.saveUser(userDetailsDTO);
        return responseBuilder.buildEmptyResponse(emptyMono, HttpStatus.NO_CONTENT);
    }
}
