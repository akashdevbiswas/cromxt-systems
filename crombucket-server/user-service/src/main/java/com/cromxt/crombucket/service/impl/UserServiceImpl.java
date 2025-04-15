package com.cromxt.crombucket.service.impl;


import com.cromxt.crombucket.dtos.requests.UserCredentials;
import com.cromxt.crombucket.dtos.requests.UserDetailsDTO;
import com.cromxt.crombucket.dtos.responses.AuthTokens;
import com.cromxt.crombucket.service.AuthService;
import com.cromxt.crombucket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
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
