package com.example.web;


import com.cromxt.crombucket.sdk.BucketUserDetails;
import com.cromxt.crombucket.sdk.clients.CromBucketWebClient;
import com.cromxt.crombucket.sdk.clients.impl.CromBucketWebClientImpl;
import com.cromxt.crombucket.sdk.users.LocalBucketUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ApplicationConfig {


    @Bean
    public BucketUserDetails cromBucketCreadentials(){
        return new LocalBucketUserDetails("http://localhost:9090");
    }

    @Bean
    public RestClient restClient(RestClient.Builder restClient) {
        return restClient.build();
    }

    @Bean
    public CromBucketWebClient cromBucketWebClient(RestClient restClient, BucketUserDetails credentials){
        return new CromBucketWebClientImpl(credentials,restClient);
    }

}
