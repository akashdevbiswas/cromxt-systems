package com.cromxt.clusterrouter.filters;


import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

@Component
@Validated
public class ClientSecretAuthorizationFilter  implements WebFilter {

//    TODO:Enable the jwt authentication.


    @Override
    public Mono<Void> filter(@Nonnull ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest serverRequest = exchange.getRequest();

        String currentPath = serverRequest.getPath().value();

        if(currentPath.startsWith("/api/v1/routes")){
            String secret = serverRequest.getHeaders().getFirst("X-Client-Secret");
            if(!isUserAuthenticated(secret)){
                exchange.getResponse().setStatusCode(HttpStatusCode.valueOf(401));
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        }

        return chain.filter(exchange);
    }

    private boolean isUserAuthenticated(String secret){
        return true;
    }
}
