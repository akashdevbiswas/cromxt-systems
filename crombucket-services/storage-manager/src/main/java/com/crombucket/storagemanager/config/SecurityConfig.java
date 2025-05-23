package com.crombucket.storagemanager.config;


import com.crombucket.storagemanager.filter.ApiKeyAuthenticationFilter;
import com.cromxt.authentication.webflux.ReactiveJwtAuthenticationFilter;
import com.cromxt.authentication.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import java.util.Objects;

@Configuration
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfig {


    private final JwtService jwtService;
    private final Environment environment;

    @Bean
    SecurityWebFilterChain webFilterChain(ServerHttpSecurity security, ApplicationContext context){

        String redirectionUrl = environment.getProperty("TOKEN_EXPIRATION_REDIRECTION_URL", String.class);
        String apiKey =  environment.getProperty("SERVICE_API_KEY",String.class);

        if(Objects.isNull(redirectionUrl) || Objects.isNull(apiKey)){
            log.error("Service Key Or Redirection URL Not found.");
            SpringApplication.exit(context,()->1);
        }

        log.info("The auth url :{} ",redirectionUrl);

        ReactiveJwtAuthenticationFilter jwtAuthenticationFilter = new ReactiveJwtAuthenticationFilter(jwtService,redirectionUrl);
        ApiKeyAuthenticationFilter apiKeyAuthenticationFilter = new ApiKeyAuthenticationFilter(apiKey);

        return security
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .anyExchange().authenticated()
                )
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.FIRST)
                .addFilterAfter(apiKeyAuthenticationFilter,SecurityWebFiltersOrder.FIRST)
                .build();
    }
}
