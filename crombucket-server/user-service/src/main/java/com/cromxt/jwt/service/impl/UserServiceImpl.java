package com.cromxt.jwt.service.impl;


import com.cromxt.jwt.dtos.requests.UserCredentials;
import com.cromxt.jwt.dtos.requests.UserDetailsDTO;
import com.cromxt.jwt.dtos.responses.AuthTokens;
import com.cromxt.jwt.service.AuthService;
import com.cromxt.jwt.service.UserService;
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
