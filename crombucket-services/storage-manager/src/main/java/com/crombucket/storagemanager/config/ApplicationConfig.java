package com.crombucket.storagemanager.config;


import com.cromxt.auth.JwtService;
import com.cromxt.auth.impl.JwtServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Mono;

@Configuration
public class ApplicationConfig {


    @Bean
    ReactiveUserDetailsService userDetailsService(){
        return username -> Mono.empty();
    }

    @Bean
    public JwtService jwtService(Environment environment){
        String secret = environment.getProperty("JWT_SECRET",String.class);
        Long expiration = environment.getProperty("JWT_EXPIRATION",Long.class);
        assert secret != null && expiration != null;
        return new JwtServiceImpl(secret,expiration);
    }
}
