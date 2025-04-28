package com.crombucket.storagemanager.filter;


import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter implements WebFilter {

    private final String apiKey;

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange,@NonNull WebFilterChain chain) {

        Mono<SecurityContext> contextMono = ReactiveSecurityContextHolder.getContext();

        return contextMono
                .flatMap(ignored -> chain.filter(exchange))
                .switchIfEmpty(Mono.defer(()->{
                    ServerHttpRequest request = exchange.getRequest();

                    String key = request.getHeaders().getFirst("X-Api-Key");
                    if(Objects.isNull(key) || !Objects.equals(key,apiKey)){
                        return chain.filter(exchange);
                    }
                    List<? extends GrantedAuthority> authority = List.of(
                            new SimpleGrantedAuthority("ROLE_SERVICE")
                    );
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("SERVICE", null, authority);
                    Context authContext = ReactiveSecurityContextHolder.withAuthentication(authenticationToken);
                    return chain.filter(exchange).contextWrite(authContext);
                }));
    }
}
