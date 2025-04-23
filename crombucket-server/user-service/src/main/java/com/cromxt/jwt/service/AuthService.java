package com.cromxt.jwt.service;


import com.cromxt.jwt.dtos.requests.UserCredentials;
import com.cromxt.jwt.dtos.responses.AuthTokens;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<AuthTokens> authenticateUserCredentials (UserCredentials userCredentials);
}
