package com.cromxt.crombucket.sdk.users;

import com.cromxt.crombucket.sdk.BucketUserDetails;


public class LocalBucketUserDetails implements BucketUserDetails {

    private final String baseUrl;
    private final String clientSecret;

    public LocalBucketUserDetails(
            String baseUrl
    ) {
        this.baseUrl = baseUrl;
        this.clientSecret = "long-client-secret";
    }

    public LocalBucketUserDetails(
            Integer bucketPort
    ) {
        this.baseUrl = String.format("http://localhost:%s", bucketPort);
        this.clientSecret = "local-client-secret";
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }
}
