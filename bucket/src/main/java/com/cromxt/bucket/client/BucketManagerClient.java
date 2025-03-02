package com.cromxt.bucket.client;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Profile({"dev", "prod"})
public class BucketManagerClient {

    private final WebClient webClient;

    public BucketManagerClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

}
