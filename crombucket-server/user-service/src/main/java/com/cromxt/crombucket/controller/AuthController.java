package com.cromxt.crombucket.controller;

import com.cromxt.crombucket.dtos.BaseResponse;
import com.cromxt.crombucket.dtos.BaseResponseBody;
import com.cromxt.crombucket.dtos.CromxtResponseStatus;
import com.cromxt.crombucket.dtos.ErrorResponse;
import com.cromxt.crombucket.dtos.requests.UserCredentials;
import com.cromxt.crombucket.dtos.requests.UserDetailsDTO;
import com.cromxt.crombucket.dtos.responses.AuthTokens;
import com.cromxt.crombucket.service.AuthService;
import com.cromxt.crombucket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping
    public Mono<ResponseEntity<AuthTokens>> login(@RequestBody UserCredentials userCredentials) {
        return authService.authenticateUserCredentials(userCredentials)
                .map((tokens) -> new ResponseEntity<>(tokens, HttpStatus.CREATED))
                .onErrorResume((err) -> {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("message", err.getMessage());
                    return Mono.just(new ResponseEntity<>(MultiValueMap.fromSingleValue(headers), HttpStatus.BAD_REQUEST));
                });
    }

    @PostMapping(value = "/register")
    public Mono<ResponseEntity<Object>> register(@RequestBody UserDetailsDTO userDetailsDTO) {
        return userService.saveUser(userDetailsDTO)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorResume(err -> {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.add("message", err.getMessage());
                    return Mono.just(ResponseEntity.badRequest().headers(httpHeaders).build());
                });
    }
}
