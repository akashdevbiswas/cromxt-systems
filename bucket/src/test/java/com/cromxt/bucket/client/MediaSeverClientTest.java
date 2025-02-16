package com.cromxt.bucket.client;

import com.cromxt.common.crombucket.dtos.mediaserver.requests.MediaStatus;
import com.cromxt.common.crombucket.dtos.mediaserver.requests.NewMediaRequest;
import com.cromxt.common.crombucket.dtos.mediaserver.requests.UpdateMediaRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class MediaSeverClientTest {

    @Autowired
    private MediaSeverClient mediaSeverClient;

    private String mediaId;


    @Test
    void shouldCreateAMediaEntityInDatabase() {

        NewMediaRequest mediaRequest = new NewMediaRequest(
                "user-1",
                "bucket-1",
                "image/jpeg",
                "file-1",
                MediaStatus.UPLOADING
        );

        mediaSeverClient.createMediaObject(mediaRequest).subscribe(
                createdMediaId->{
                    assertNotNull(mediaId);
                    this.mediaId = createdMediaId;
                }
        );

    }

    @Test
    void shouldUpdateAMediaEntityInDatabase() {
        UpdateMediaRequest mediaRequest = new UpdateMediaRequest(
                100L
        );

        mediaSeverClient.updateMediaObject(this.mediaId,mediaRequest).subscribe();
    }
}