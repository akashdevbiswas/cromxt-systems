package com.cromxt.bucket.config;


import com.cromxt.bucket.service.impl.BucketInformationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BucketConfig {


    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return WebClient.builder().build();
    }


}
