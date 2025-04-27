package com.crombucket.storagemanager.config;


import com.crombucket.storagemanager.filter.ApiKeyAuthenticationFilter;
import com.cromxt.auth.JwtAuthenticationFilter;
import com.cromxt.auth.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@RequiredArgsConstructor
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfig {


    private final JwtService jwtService;
    private final Environment environment;

    @Bean
    SecurityWebFilterChain webFilterChain(ServerHttpSecurity security){

        String redirectionUrl = environment.getProperty("TOKEN_EXPIRATION_REDIRECTION_URL", String.class);
        String apiKey =  environment.getProperty("SERVICE_API_KEY",String.class);

        assert redirectionUrl != null && apiKey!=null;


        log.info("The auth url :{} ",redirectionUrl);

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService,redirectionUrl);
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
