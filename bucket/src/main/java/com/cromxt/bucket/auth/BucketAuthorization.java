package com.cromxt.bucket.auth;

public interface BucketAuthorization {

    boolean isRequestAuthorized(String secret);

    String extractClientId(String secret);
}
