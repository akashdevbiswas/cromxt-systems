package com.cromxt.bucket.client;


import com.cromxt.common.crombucket.dtos.mediaserver.requests.NewMediaRequest;
import com.cromxt.common.crombucket.dtos.mediaserver.requests.UpdateMediaRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;


@Service
@Slf4j
public class MediaSeverClient {

    private final WebClient webClient;
    private final String API_KEY;
    private final String mediaClientUrl;

    public MediaSeverClient(WebClient.Builder webClientBuilder,
                            Environment environment) {
        mediaClientUrl = environment.getProperty("BUCKET_CONFIG_MEDIA_CLIENT_URL", String.class);

        this.API_KEY = environment.getProperty("BUCKET_CONFIG_API_KEY", String.class);

        assert mediaClientUrl != null;

        this.webClient = WebClient.builder()
                .build();
    }


    public Mono<String> createMediaObject(
            NewMediaRequest mediaDetails
    ){
        return webClient
                .post()
                .uri(URI.create(mediaClientUrl))
                .header("Api-Key",API_KEY)
                .bodyValue(mediaDetails)
                .retrieve()
                .onStatus(HttpStatusCode::isError,clientResponse -> {
                    log.error("Error occurred while creating media object");
                    return Mono.error(new RuntimeException("Some error Occurred"));
                })
                .bodyToMono(String.class);
    }

    public Mono<Void> updateMediaObject(
            String mediaId,
            UpdateMediaRequest updateMediaDetails
    ){
        return webClient
                .put()
                .uri(URI.create(mediaClientUrl+"/"+mediaId))
                .header("Api-Key",API_KEY)
                .bodyValue(updateMediaDetails)
                .retrieve()
                .onStatus(HttpStatusCode::isError,clientResponse -> {
                    log.error("Error occurred while update the media object");
                    return Mono.error(new RuntimeException("Some error Occurred"));
                })
                .bodyToMono(Void.class);
    }

    public Mono<Void> deleteMediaObject(String mediaId){
        return webClient
                .delete()
                .uri(URI.create(mediaClientUrl+"/"+mediaId))
                .header("Api-Key",API_KEY)
                .retrieve()
                .onStatus(HttpStatusCode::isError,clientResponse -> {
                    log.error("Error occurred while delete the media object");
                    return Mono.error(new RuntimeException("Some error Occurred"));
                })
                .bodyToMono(Void.class);
    }


}
