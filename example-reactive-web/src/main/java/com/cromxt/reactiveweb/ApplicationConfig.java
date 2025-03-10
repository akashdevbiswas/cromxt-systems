package com.cromxt.reactiveweb;


import com.cromxt.toolkit.crombucket.CromBucketCreadentials;
import com.cromxt.toolkit.crombucket.clients.ReactiveCromBucketClient;
import com.cromxt.toolkit.crombucket.clients.impl.ReactiveCromBucketClientImpl;
import com.cromxt.toolkit.crombucket.creadentials.LocalBucketCredential;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApplicationConfig {


    @Bean
    public CromBucketCreadentials cromBucketCreadentials() {
        return new LocalBucketCredential("http://localhost:9090");
    }

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.build();
    }

    @Bean
    public ReactiveCromBucketClient reactiveCromBucketClient(WebClient webClient, CromBucketCreadentials credentials) {
        return new ReactiveCromBucketClientImpl(credentials, webClient);
    }


}
