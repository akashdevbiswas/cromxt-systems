package com.cromxt.bucket.client;


import com.cromxt.bucket.models.MediaObjects;
import com.cromxt.common.crombucket.dtos.mediaserver.requests.UpdateMediaRequestDTO;
import com.cromxt.common.crombucket.dtos.mediaserver.response.MediaEntityDTO;
import com.cromxt.common.crombucket.dtos.mediaserver.response.NewMediaResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;


@Service
@Slf4j
@Profile({"dev","prod"})
public class MediaSeverClient {

    private final WebClient webClient;
    private final String apiKey;
    private final String mediaClientBaseUrl;

    public MediaSeverClient(WebClient.Builder webClientBuilder,
                            Environment environment) {
        String mediaClientUrl = environment.getProperty("BUCKET_CONFIG_MEDIA_CLIENT_URL", String.class);
        String key = environment.getProperty("BUCKET_CONFIG_API_KEY", String.class);

        assert mediaClientUrl != null && key != null;

        this.apiKey = key;
        this.mediaClientBaseUrl = mediaClientUrl;
        this.webClient = WebClient.builder().build();
    }


    public Mono<NewMediaResponseDTO> createMediaObject(String clientId){
        String url = String.format("%s/%s",mediaClientBaseUrl,clientId);
        return webClient
                .post()
                .uri(URI.create(url))
                .header("Api-Key",apiKey)
                .retrieve()
                .onStatus(HttpStatusCode::isError,clientResponse -> {
                    log.error("Error occurred while creating media object");
                    return Mono.error(new RuntimeException("Some error Occurred"));
                })
                .bodyToMono(NewMediaResponseDTO.class);
    }

    public Mono<MediaEntityDTO> updateMediaObject(
            String mediaId,
            UpdateMediaRequestDTO updateMediaDetails
    ){
//      TODO:  Update this method to get the media entity from the media server.
        return webClient
                .put()
                .uri(URI.create(mediaClientBaseUrl+"/"+mediaId))
                .header("Api-Key",apiKey)
                .bodyValue(updateMediaDetails)
                .retrieve()
                .onStatus(HttpStatusCode::isError,clientResponse -> {
                    log.error("Error occurred while update the media object");
                    return Mono.error(new RuntimeException("Some error Occurred"));
                })
                .bodyToMono(MediaEntityDTO.class);
    }

    public Mono<Void> deleteMediaObject(String mediaId){
        return webClient
                .delete()
                .uri(URI.create(mediaClientBaseUrl+"/"+mediaId))
                .header("Api-Key",apiKey)
                .retrieve()
                .onStatus(HttpStatusCode::isError,clientResponse -> {
                    log.error("Error occurred while delete the media object");
                    return Mono.error(new RuntimeException("Some error Occurred"));
                })
                .bodyToMono(Void.class);
    }


    public Mono<Long> getAvailableSpaceUserHave(String clientId) {
//        Implement the method to get the available space user have.
        return Mono.just(0L);
    }

    public static UpdateMediaRequestDTO createUpdateMediaRequest(MediaObjects mediaObjects){
        return new UpdateMediaRequestDTO(
                mediaObjects.getFileId(),
                mediaObjects.getFileSize(),
                mediaObjects.getExtension(),
                mediaObjects.getAccessUrl()
        );
    }
}
