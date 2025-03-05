package com.cromxt.bucket.service.impl;

import com.cromxt.bucket.service.AccessURLGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("local")
@RequiredArgsConstructor
@Service
public class LocalDevAccessUrlGenerator implements AccessURLGenerator {

    private final BucketInformationService bucketInformationService;

    @Override
    public String generateAccessURL(String fileName) {
        String hostName = "localhost";
        Integer port = bucketInformationService.getHttpPort();
        return String.format("http://%s:%s/api/v1/objects/%s", hostName, port, fileName);
    }
}
