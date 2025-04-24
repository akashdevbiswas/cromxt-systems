package com.cromxt.jwt.service.impl;


import com.cromxt.jwt.requests.UserCredentials;
import com.cromxt.jwt.requests.UserDetailsDTO;
import com.cromxt.jwt.responses.AuthTokens;
import com.cromxt.jwt.service.AuthService;
import com.cromxt.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class UserServiceImpl implements AuthService, UserService {

    private final ReactiveAuthenticationManager authenticationManager;

    @Override
    public Mono<AuthTokens> authenticateUserCredentials(UserCredentials userCredentials) {
        return null;
    }

    @Override
    public Mono<Void> saveUser(UserDetailsDTO userDetailsDTO) {
        return null;
    }
}
