package com.cromxt.auth.service;


import com.cromxt.auth.requests.UserCredentials;
import com.cromxt.auth.responses.AuthTokens;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<AuthTokens> authenticateUserCredentials (UserCredentials userCredentials);
}
