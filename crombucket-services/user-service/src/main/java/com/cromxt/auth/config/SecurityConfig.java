package com.cromxt.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import com.cromxt.authentication.JwtService;
import com.cromxt.authentication.webflux.ReactiveJwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
public class SecurityConfig {

        private final ReactiveAuthenticationManager reactiveAuthenticationManager;
        private final JwtService jwtService;
        private final Environment environment;

        private static final String[] whitelistUrl = {
                        "/api/v1/auth/**"
        };

        @Bean
        public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
                String tokenExpirationRedirectionUrl = environment.getProperty("TOKEN_EXPIRATION_REDIRECTION_URL",
                                String.class);

                ReactiveJwtAuthenticationFilter jwtFilter = new ReactiveJwtAuthenticationFilter(jwtService,
                                tokenExpirationRedirectionUrl);

                return serverHttpSecurity
                                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                                                .pathMatchers(whitelistUrl).permitAll()
                                                .anyExchange().authenticated())
                                .exceptionHandling((exceptionHandlingSpec) -> exceptionHandlingSpec
                                                .authenticationEntryPoint((webFilterExchange, authException) -> {
                                                        // Handle authentication failures.
                                                        // This configuration will send only the UNAUTHORIZED status
                                                        // code
                                                        // not the WWW-Authenticate header, which causes the browser to
                                                        // show the login popup.
                                                        webFilterExchange.getResponse()
                                                                        .setStatusCode(HttpStatus.UNAUTHORIZED);
                                                        return webFilterExchange.getResponse().setComplete();
                                                }))
                                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                                .authenticationManager(reactiveAuthenticationManager)
                                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.FIRST)
                                .build();
        }
}
