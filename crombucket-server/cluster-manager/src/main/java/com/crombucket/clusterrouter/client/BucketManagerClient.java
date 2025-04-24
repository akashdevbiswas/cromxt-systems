package com.crombucket.clusterrouter.client;

import com.crombucket.clusterrouter.utils.RoutingProperties;
import com.crombucket.common.systemmanager.StorageServerRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Slf4j
public class BucketManagerClient {

    private final WebClient webClient;
    private final RoutingProperties routingProperties;


    public Flux<StorageServerRequest> getBucketObjects() {
        URI url = URI.create(
                String.format("%s/%s", routingProperties.getBucketManagerAddress(), routingProperties.getClusterId())
        );
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .onStatus(HttpStatusCode::isError,clientResponse -> {
                    return Mono.error(new RuntimeException("Some error occurred while fetch the buckets."));
                })
                .bodyToFlux(StorageServerRequest.class)
                .onErrorResume(err->{
                    log.error("Error occurred while fetching the buckets {}", err.getMessage());
                    return Flux.empty();
                });
    }


}
