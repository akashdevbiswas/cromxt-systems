package com.cromxt.storageservice.service.impl;


import com.cromxt.storageservice.service.AccessURLGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"crombucket","crombucket-docker","crombucket-docker-dev"})
@RequiredArgsConstructor
public class CrombucketAccessUrlGenerator implements AccessURLGenerator {

    private final StorageServerDetails storageServerDetails;

    @Override
    public String generateAccessURL(String fileId) {
        return "aLongURL";
    }
}
