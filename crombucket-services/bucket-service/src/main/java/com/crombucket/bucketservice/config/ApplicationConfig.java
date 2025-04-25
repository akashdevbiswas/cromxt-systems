package com.crombucket.bucketservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.cromxt.auth.JwtService;
import com.cromxt.auth.impl.JwtServiceImpl;

@Configuration
public class ApplicationConfig {

    @Bean
    JwtService jwtService(Environment environment){
        String secret = environment.getProperty("JWT_SECRET",String.class);
        Long expiration = environment.getProperty("JWT_EXPIRATION",Long.class);
        return new JwtServiceImpl(secret,expiration);
    }

}
