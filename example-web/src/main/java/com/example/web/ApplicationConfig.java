package com.example.web;


import com.cromxt.toolkit.crombucket.CromBucketCreadentials;
import com.cromxt.toolkit.crombucket.clients.CromBucketClient;
import com.cromxt.toolkit.crombucket.clients.CromBucketWebClient;
import com.cromxt.toolkit.crombucket.clients.ReactiveCromBucketClient;
import com.cromxt.toolkit.crombucket.clients.impl.CromBucketWebClientImpl;
import com.cromxt.toolkit.crombucket.clients.impl.ReactiveCromBucketClientImpl;
import com.cromxt.toolkit.crombucket.creadentials.LocalBucketCredential;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApplicationConfig {


    @Bean
    public CromBucketCreadentials cromBucketCreadentials(){
        return new LocalBucketCredential("http://localhost:9090");
    }

    @Bean
    public RestClient restClient(RestClient.Builder restClient) {
        return restClient.build();
    }

    @Bean
    public CromBucketWebClient cromBucketWebClient(RestClient restClient, CromBucketCreadentials credentials){
        return new CromBucketWebClientImpl(credentials,restClient);
    }

}
