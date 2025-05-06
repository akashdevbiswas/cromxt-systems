package com.crombucket.mediaservice.clients;

import java.net.URI;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.crombucket.common.bucketservice.responses.StorageNodeDetails;
import com.crombucket.common.mediaservice.requests.MediaObjectInitializeRequest;

import reactor.core.publisher.Mono;

@Service
public class BucketServiceClient {


    private final WebClient webClient;

    public BucketServiceClient(WebClient webClient) {
        this.webClient = webClient;
    }
    
    public Mono<StorageNodeDetails> fetchStorageNode(MediaObjectInitializeRequest mediaObjectRequest){
        return webClient
        .post()
        .uri(URI.create("http://localhost:8902/api/v1/buckets"))
        .bodyValue(mediaObjectRequest)
        .retrieve()
        .onStatus(HttpStatusCode::isError,(res)->{
            return Mono.just(new RuntimeException());
        })
        .bodyToMono(StorageNodeDetails.class);
    }

    public Mono<StorageNodeDetails> fetchStorageNode(String nodeId){
        return null;
    }

}
