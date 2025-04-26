package com.crombucket.bucketservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.HttpBasicSpec;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import com.cromxt.auth.JwtAuthenticationFilter;
import com.cromxt.auth.JwtService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final JwtService jwtService;

    private static final String[] WHITELIST_GET_URLS = {
            "/api/v1/buckets/types"
    };

    @Bean
    public SecurityWebFilterChain webFilterChain(ServerHttpSecurity serverSecurity, Environment environment) {

        String location = environment.getProperty("TOKEN_EXPIRATION_REDIRECTION_URL", String.class);

        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtService, location);
        return serverSecurity
                .csrf(CsrfSpec::disable)
                .formLogin(FormLoginSpec::disable)
                .httpBasic(HttpBasicSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers(HttpMethod.GET, WHITELIST_GET_URLS)
                        .permitAll()
                        .anyExchange().authenticated())
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.FIRST)
                .build();
    }

    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService() {
        return username -> {
            return Mono.empty();
        };
    }
}
