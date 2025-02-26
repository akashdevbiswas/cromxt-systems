package com.cromxt.routeservice.client;


import com.cromxt.common.crombucket.kafka.BucketObject;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class SystemManagerClient {

    private final String SERVER_CLIENT_BASE_URL;
    private final WebClient webClient;

    public SystemManagerClient(
            WebClient.Builder webClient,
            Environment environment
    ) {

        String url = environment.getProperty("ROUTE_SERVICE_CONFIG_SYSTEM_MANAGER_CLIENT_ADDRESS", String.class);
        assert url != null;
        this.SERVER_CLIENT_BASE_URL = url;
        this.webClient = webClient.build();
    }

    public Flux<BucketObject> getBucketObjects(){
        return webClient
                .get()
                .uri("http://"+SERVER_CLIENT_BASE_URL + "/system-manager/service/v1/buckets")
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new RuntimeException("Some error occurred.")))
                .bodyToFlux(BucketObject.class);
    }
}
