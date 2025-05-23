package com.crombucket.storageservice.client;


import com.crombucket.common.mediaservice.requests.MediaRequest;
import com.crombucket.common.mediaservice.response.MediaObjects;
import com.crombucket.common.mediaservice.response.UpdateMediaUploadStatusRequest;
import com.crombucket.storageservice.constants.FileVisibility;
import com.crombucket.storageservice.models.FileObjects;
import com.crombucket.storageservice.service.AccessURLGenerator;
import com.crombucket.storageservice.service.impl.StorageServerDetails;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
@Profile({"crombucket","crombucket-docker","crombucket-docker-dev"})
@Slf4j
public class RemoteMediaManagerClient implements MediaManagerClient {

    private final WebClient webClient;
    private final String mediaClientBaseUrl;
    private final String apiKey;
    private final StorageServerDetails storageServerDetails;
    private final String clusterId;
    private final String regionId;

    public RemoteMediaManagerClient(WebClient webClient,
                                    AccessURLGenerator accessURLGenerator,
                                    StorageServerDetails storageServerDetails,
                                    Environment environment) {
        String clientUrl = environment.getProperty("STORAGE_SERVER_MEDIA_CLIENT_URL", String.class);
        String apiKey = environment.getProperty("STORAGE_SERVER_MEDIA_CLIENT_API_KEY", String.class);
        String clusterId = environment.getProperty("STORAGE_SERVER_CLUSTER_ID", String.class);
        String regionId = environment.getProperty("STORAGE_SERVER_REGION_ID", String.class);
        assert clientUrl != null && apiKey != null && clusterId != null && regionId != null;

        this.mediaClientBaseUrl = clientUrl;
        this.apiKey = apiKey;
        this.webClient = webClient;
        this.clusterId = clusterId;
        this.regionId = regionId;
        this.storageServerDetails = storageServerDetails;
    }

    @Override
    public Mono<String> createMediaObject(String clientId, FileVisibility visibility) {
        MediaRequest mediaRequest = MediaRequest.builder()
                .clientId(clientId)
                .clusterId(clusterId)
                .regionId(regionId)
                .bucketId(storageServerDetails.getBucketId())
                .visibility(visibility.name())
                .build();
        String url = String.format("%s/api/v1/medias", mediaClientBaseUrl);

        return webClient
                .post()
                .uri(URI.create(url))
                .header("Api-Key", apiKey)
                .bodyValue(mediaRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.error("Error occurred while creating media object");
                    return Mono.error(new RuntimeException("Some error Occurred"));
                })
                .bodyToMono(String.class);
    }

    @Override
    public Mono<MediaObjects> updateMediaUploadStatus(String mediaId, String accessUrl, FileObjects fileObjects) {

        String fileId = fileObjects.getFileId();

        UpdateMediaUploadStatusRequest updateMediaUploadStatusRequest = UpdateMediaUploadStatusRequest.builder()
                .fileSize(fileObjects.getFileSize())
                .fileId(fileId)
                .accessUrl(accessUrl)
                .visibility(fileObjects.getVisibility().name())
                .build();

        String url = String.format("%s/service/v1/medias/%s", mediaClientBaseUrl, mediaId);
        return webClient
                .put()
                .uri(URI.create(url))
                .header("Api-Key", apiKey)
                .bodyValue(updateMediaUploadStatusRequest)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {
                    log.error("Error occurred while updating media object");
                    return Mono.error(new RuntimeException("Some error Occurred"));
                })
                .bodyToMono(MediaObjects.class);
    }


}
