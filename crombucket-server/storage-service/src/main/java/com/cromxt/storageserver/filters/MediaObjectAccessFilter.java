package com.cromxt.storageserver.filters;


import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Profile({"crombucket","crombucket-docker","crombucket-docker-dev"})
public class MediaObjectAccessFilter implements WebFilter {


    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {

        String path = exchange.getRequest().getPath().value();
        String prefix = "/api/v1/objects";

        System.out.println(path);

            return chain.filter(exchange);
        }
    }
