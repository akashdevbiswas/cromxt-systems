package com.example.web;


import com.cromxt.toolkit.crombucket.BucketUserDetails;
import com.cromxt.toolkit.crombucket.clients.CromBucketWebClient;
import com.cromxt.toolkit.crombucket.clients.impl.CromBucketWebClientImpl;
import com.cromxt.toolkit.crombucket.users.LocalBucketUserDetails;
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
