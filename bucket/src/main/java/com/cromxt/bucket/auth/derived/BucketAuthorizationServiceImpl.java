package com.cromxt.bucket.auth.derived;


import com.cromxt.bucket.auth.BucketAuthorization;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"dev","prod"})
@RequiredArgsConstructor
public class BucketAuthorizationServiceImpl implements BucketAuthorization {
//    TODO: Implement the JWT Authorization for this.
    @Override
    public boolean isRequestAuthorized(String secret) {
        return true;
    }

    @Override
    public String extractClientId(String secret) {
        return "longusername";
    }

    @Override
    public String getApiKey() {
        return "api-key";
    }
}
