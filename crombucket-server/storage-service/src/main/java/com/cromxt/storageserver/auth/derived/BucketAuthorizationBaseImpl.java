package com.cromxt.storageserver.auth.derived;


import com.cromxt.storageserver.auth.BucketAuthorizationBase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"crombucket","crombucket-docker","crombucket-docker-dev"})
@RequiredArgsConstructor
public class BucketAuthorizationBaseImpl implements BucketAuthorizationBase {
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
