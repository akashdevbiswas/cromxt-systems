package com.cromxt.bucket.repository.impl;


import com.cromxt.bucket.models.FileObjects;
import com.cromxt.bucket.models.MediaObjects;
import com.cromxt.bucket.repository.MediaManager;
import com.cromxt.bucket.service.AccessURLGenerator;
import com.cromxt.bucket.service.impl.BucketInformationService;
import com.cromxt.common.crombucket.mediamanager.requests.NewMediaRequest;
import com.cromxt.common.crombucket.mediamanager.requests.UpdateMediaRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
@Profile({"prod","dev"})
@Slf4j
public class RemoteMediaManager extends MediaManager {

    private final WebClient webClient;
    private final String mediaClientBaseUrl;
    private final String apiKey;

    public RemoteMediaManager(WebClient webClient,
                              AccessURLGenerator accessURLGenerator,
                              BucketInformationService bucketInformationService,
                              Environment environment) {

        super(accessURLGenerator);
        String clientUrl = environment.getProperty("BUCKET_CONFIG_MEDIA_CLIENT_URL", String.class);
        String apiKey = environment.getProperty("BUCKET_CONFIG_MEDIA_CLIENT_API_KEY", String.class);
        assert clientUrl != null && apiKey != null;

        this.mediaClientBaseUrl = clientUrl;
        this.apiKey = apiKey;
        this.webClient = webClient;
    }

    @Override
    public Mono<MediaObjects> createMediaObject(FileObjects fileObjects){
        String url = String.format("%s/api/v1/medias",mediaClientBaseUrl);
        NewMediaRequest newMediaRequest = NewMediaRequest.builder()
                .build();
        return webClient
                .post()
                .uri(URI.create(url))
                .header("Api-Key",apiKey)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.error("Error occurred while creating media object");
                    return Mono.error(new RuntimeException("Some error Occurred"));
                })
                .bodyToMono(MediaObjects.class);
    }

    @Override
    public Mono<MediaObjects> updateMedia(
            String mediaObjectId,
            FileObjects fileObjects
    ){
        String url = String.format("%s/api/v1/medias/%s",mediaClientBaseUrl,mediaObjectId);
        String accessUrl = accessURLGenerator.generateAccessURL(fileObjects.getFileId());
        UpdateMediaRequestDTO updateMediaRequest = new UpdateMediaRequestDTO(
                fileObjects.getFileId(),
                fileObjects.getFileSize(),
                fileObjects.getExtension(),
                accessUrl
        );
        return webClient
                .put()
                .uri(URI.create(url))
                .header("Api-Key",apiKey)
                .bodyValue(updateMediaRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError,clientResponse -> {
                    log.error("Error occurred while update the media object");
                    return Mono.error(new RuntimeException("Some error Occurred"));
                })
                .bodyToMono(MediaObjects.class);
    }

    @Override
    public Mono<Void> deleteMedia(String mediaId){
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

}
