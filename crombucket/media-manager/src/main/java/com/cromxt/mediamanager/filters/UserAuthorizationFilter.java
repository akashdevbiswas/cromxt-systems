package com.cromxt.mediamanager.filters;

import jakarta.annotation.Nonnull;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Validated
public class UserAuthorizationFilter implements WebFilter {
    @Override
    public Mono<Void> filter(@Nonnull ServerWebExchange exchange,@Nonnull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        System.out.println("Request: " + request.getMethod() + " " + request.getURI());


        return chain.filter(exchange);
    }
}
