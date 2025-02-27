package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.service.AccessURLGenerator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("local")
public class LocalAccessURLGenerator implements AccessURLGenerator {
    private final String baseAccessUrl;

    public LocalAccessURLGenerator(BucketInformationService bucketInformationService) {
        this.baseAccessUrl = String.format("http://localhost:%s/api/v1/objects/", bucketInformationService.getHttpPort());
    }

    @Override
    public String generateAccessURL(String mediaId) {
        return String.format("%s%s", baseAccessUrl, mediaId);
    }
}
