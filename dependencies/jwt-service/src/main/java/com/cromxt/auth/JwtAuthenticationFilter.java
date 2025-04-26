package com.cromxt.auth;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Objects;


@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;
    private final String location;

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        Mono<SecurityContext> securityContext = ReactiveSecurityContextHolder.getContext();

        return securityContext
                .flatMap(ctx -> chain.filter(exchange))
                .switchIfEmpty(
                        Mono.defer(() -> {
                                    ServerHttpRequest request = exchange.getRequest();

                                    String authHeader = request.getHeaders().getFirst("Authorization");

                                    if (Objects.nonNull(authHeader) && authHeader.startsWith("Bearer ")) {

                                        String token = authHeader.substring(7);

                                        try{
                                            UserDetails userDetails = jwtService.extractUserDetails(token);
                                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
                                            Context authContext = ReactiveSecurityContextHolder.withAuthentication(authenticationToken);
                                            return chain.filter(exchange).contextWrite(authContext);
                                        }catch (ExpiredJwtException expiredJwtToken){
                                            ServerHttpResponse response = exchange.getResponse();
                                            response.getHeaders().add("message",expiredJwtToken.getMessage());
                                            response.getHeaders().add("Location", location);
                                            response.setRawStatusCode(HttpStatus.SEE_OTHER.value());
                                            return response.setComplete();
                                        }
                                        catch (Exception exception){
                                            log.error("Error occurred while validate the token {}",exception.getMessage());
                                        }
                                    }
                                    return chain.filter(exchange);
                                }
                        )
                );
    }
}
