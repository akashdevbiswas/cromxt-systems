package com.crombucket.storageservice.service.impl;

import com.crombucket.storageservice.service.AccessURLGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile({"local","local-docker","local-docker-dev"})
@RequiredArgsConstructor
@Service
public class LocalAccessUrlGenerator implements AccessURLGenerator {

    private final StorageServerDetails storageServerDetails;

    @Override
    public String generateAccessURL(String fileName) {
        String hostName = "localhost";
        Integer port = storageServerDetails.getHttpPort();
        return String.format("http://%s:%s/api/v1/files/%s", hostName, port, fileName);
    }
}
