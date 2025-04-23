package com.cromxt.jwt.auth;

import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Collections;


public class JWTAuthenticationFilter implements WebFilter {
    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {

        Mono<SecurityContext> securityContext = ReactiveSecurityContextHolder.getContext();

        System.out.println("Executed JWT filter.");

        return securityContext
                .flatMap(ctx -> chain.filter(exchange))
                .switchIfEmpty(Mono.defer(() -> {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("akash", null, Collections.emptyList());
                    Context authContext = ReactiveSecurityContextHolder.withAuthentication(authenticationToken);
                    return chain.filter(exchange).contextWrite(authContext);
                }));
    }
}
