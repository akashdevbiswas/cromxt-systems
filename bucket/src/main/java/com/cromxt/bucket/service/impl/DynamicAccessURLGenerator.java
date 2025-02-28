package com.cromxt.bucket.service.impl;


import com.cromxt.bucket.service.AccessURLGenerator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Profile({"dev","prod"})
public class DynamicAccessURLGenerator implements AccessURLGenerator {

    private final WebClient webClient;


    public DynamicAccessURLGenerator(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }


    @Override
    public Mono<String> generateAccessURL(String mediaId) {
        return Mono.just("aLongURL");
    }
}
