package com.cromxt.crombucket.service;


import com.cromxt.crombucket.dtos.requests.UserCredentials;
import com.cromxt.crombucket.dtos.responses.AuthTokens;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<AuthTokens> authenticateUserCredentials (UserCredentials userCredentials);
}
