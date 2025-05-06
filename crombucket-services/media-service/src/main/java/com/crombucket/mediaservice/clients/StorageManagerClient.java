package com.crombucket.mediaservice.clients;

import java.net.URI;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import com.crombucket.common.bucketservice.responses.StorageNodeDetails;

import reactor.core.publisher.Mono;

public class StorageManagerClient {
    private final WebClient webClient;


    public StorageManagerClient(Environment environment, WebClient.Builder webClientBuilder) {
        String storageManagerUrl = environment.getProperty("STORAGE_MANAGER_URL", String.class);
        this.webClient = webClientBuilder.build();
    }

    public Mono<StorageNodeDetails> getStorageNodeDetails(String clusterID) {
        return webClient
        .get()
        .uri(URI.create("http://localhost:8902/api/v1/storagenodes/"+clusterID))
        .retrieve()
        .onStatus(HttpStatusCode::isError,(res)->{
            return Mono.just(new RuntimeException());
        })
        .bodyToMono(StorageNodeDetails.class);
    }
}
