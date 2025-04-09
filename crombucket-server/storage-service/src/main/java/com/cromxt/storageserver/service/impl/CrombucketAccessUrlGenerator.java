package com.cromxt.storageserver.service.impl;


import com.cromxt.storageserver.service.AccessURLGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"crombucket","crombucket-docker","crombucket-docker-dev"})
@RequiredArgsConstructor
public class CrombucketAccessUrlGenerator implements AccessURLGenerator {

    private final BucketInformationService bucketInformationService;

    @Override
    public String generateAccessURL(String fileId) {
        return "aLongURL";
    }
}
