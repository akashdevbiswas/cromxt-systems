package com.cromxt.jwt.service;


import com.cromxt.jwt.requests.UserCredentials;
import com.cromxt.jwt.responses.AuthTokens;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<AuthTokens> authenticateUserCredentials (UserCredentials userCredentials);
}
