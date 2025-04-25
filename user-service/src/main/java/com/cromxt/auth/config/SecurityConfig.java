package com.cromxt.auth.config;


import com.cromxt.auth.JwtAuthenticationFilter;
import com.cromxt.auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

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
        String tokenExpirationRedirectionUrl = environment.getProperty("TOKEN_EXPIRATION_REDIRECTION_URL", String.class,"http://localhost:4200/login");

        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtService,tokenExpirationRedirectionUrl);

        return serverHttpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(authorizeExchangeSpec ->
                        authorizeExchangeSpec
                                .pathMatchers(whitelistUrl).permitAll()
                                .anyExchange().authenticated()
                )
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authenticationManager(reactiveAuthenticationManager)
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.FIRST)
                .build();
    }
}
