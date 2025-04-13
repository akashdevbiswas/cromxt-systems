package com.cromxt.crombucket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final ReactiveAuthenticationManager reactiveAuthenticationManager;

    @GetMapping
    public Mono<String> login(){

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                "akash",
                "password"
        );

        Mono<Authentication> authentication = reactiveAuthenticationManager.authenticate(authenticationToken);

        return authentication.flatMap(auth->{
            if(!auth.isAuthenticated()){
                return Mono.just("Invalid credentials");
            }
            return Mono.just("A long token");
        }).onErrorResume((err)-> Mono.just("Error occurred "+ err.getMessage()));
    }
}
