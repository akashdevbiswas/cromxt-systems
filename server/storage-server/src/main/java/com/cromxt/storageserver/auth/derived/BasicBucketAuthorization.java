package com.cromxt.storageserver.auth.derived;

import com.cromxt.storageserver.auth.BucketAuthorization;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;


@Profile("local")
@Service
public class BasicBucketAuthorization implements BucketAuthorization {
    @Override
    public boolean isRequestAuthorized(String secret) {
        return secret.equals("long-client-secret");
    }

    @Override
    public String extractClientId(String secret) {
        return "local-client";
    }

}
