package com.crombucket.storagemanager.filter;

import java.security.Security;
import java.util.List;
import java.util.Objects;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter implements WebFilter {

    private final String apiKey;

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        Mono<SecurityContext> contextMono = ReactiveSecurityContextHolder.getContext();
        return contextMono
        .switchIfEmpty(Mono.defer(()-> {
            System.out.println("This is ApiKeyAuthenticationFilter");
            return Mono.just(new SecurityContextImpl());
        }))
        .flatMap(context->{
            if (context.getAuthentication() != null) {
                return chain.filter(exchange);
            }
            return verifyApiKey(exchange, chain);
        });
                
    }

    private Mono<Void> verifyApiKey(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String key = request.getHeaders().getFirst("X-Api-Key");
        if (Objects.isNull(key) || !Objects.equals(key, apiKey)) {
            return chain.filter(exchange);
        }
        List<? extends GrantedAuthority> authority = List.of(
                new SimpleGrantedAuthority("ROLE_SERVICE"));
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken("SERVICE",
                null, authority);

        SecurityContext context = new SecurityContextImpl(authenticationToken);
        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
    }
}
