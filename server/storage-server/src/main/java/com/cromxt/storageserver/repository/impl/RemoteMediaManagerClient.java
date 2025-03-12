package com.cromxt.storageserver.repository.impl;


import com.cromxt.storageserver.client.MediaManagerClient;
import com.cromxt.storageserver.constants.FileVisibility;
import com.cromxt.storageserver.models.FileObjects;
import com.cromxt.storageserver.service.AccessURLGenerator;
import com.cromxt.storageserver.service.impl.BucketInformationService;
import com.cromxt.common.crombucket.mediamanager.requests.MediaRequest;
import com.cromxt.common.crombucket.mediamanager.response.MediaObjects;
import com.cromxt.common.crombucket.mediamanager.response.UpdateMediaUploadStatusRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
@Profile({"prod", "dev"})
@Slf4j
public class RemoteMediaManagerClient implements MediaManagerClient {

    private final WebClient webClient;
    private final String mediaClientBaseUrl;
    private final String apiKey;
    private final BucketInformationService bucketInformationService;
    private final String clusterId;
    private final String regionId;
    private final AccessURLGenerator accessURLGenerator;

    public RemoteMediaManagerClient(WebClient webClient,
                                    AccessURLGenerator accessURLGenerator,
                                    BucketInformationService bucketInformationService,
                                    Environment environment) {
        String clientUrl = environment.getProperty("STORAGE_SERVER_MEDIA_CLIENT_URL", String.class);
        String apiKey = environment.getProperty("STORAGE_SERVER_MEDIA_CLIENT_API_KEY", String.class);
        String clusterId = environment.getProperty("STORAGE_SERVER_CLUSTER_ID", String.class);
        String regionId = environment.getProperty("STORAGE_SERVER_REGION_ID", String.class);
        assert clientUrl != null && apiKey != null && clusterId != null && regionId != null;

        this.accessURLGenerator = accessURLGenerator;
        this.mediaClientBaseUrl = clientUrl;
        this.apiKey = apiKey;
        this.webClient = webClient;
        this.clusterId = clusterId;
        this.regionId = regionId;
        this.bucketInformationService = bucketInformationService;
    }

    @Override
    public Mono<String> createMediaObject(String clientId, FileVisibility visibility) {
        MediaRequest mediaRequest = MediaRequest.builder()
                .clientId(clientId)
                .clusterId(clusterId)
                .regionId(regionId)
                .bucketId(bucketInformationService.getBucketId())
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
