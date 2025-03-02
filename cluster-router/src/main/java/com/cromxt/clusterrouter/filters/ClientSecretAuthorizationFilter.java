package com.cromxt.clusterrouter.filters;


import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

@Validated
public class ClientSecretAuthorizationFilter  implements WebFilter {


    @Override
    public Mono<Void> filter(@Nonnull ServerWebExchange exchange, WebFilterChain chain) {

        return chain.filter(exchange);
    }
}
