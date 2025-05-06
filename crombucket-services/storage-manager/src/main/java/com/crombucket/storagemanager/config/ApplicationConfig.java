package com.crombucket.storagemanager.config;


import com.crombucket.common.ResponseBuilder;
import com.crombucket.common.ResponseBuilderImpl;
import com.cromxt.authentication.JwtService;
import com.cromxt.authentication.JwtServiceImpl;
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
    JwtService jwtService(Environment environment){
        String secret = environment.getProperty("JWT_SECRET",String.class);
        Long expiration = environment.getProperty("JWT_EXPIRATION",Long.class);
        assert secret != null && expiration != null;
        return new JwtServiceImpl(secret,expiration);
    }

    @Bean
    ResponseBuilder utilityService(){
        return new ResponseBuilderImpl();
    }


}
