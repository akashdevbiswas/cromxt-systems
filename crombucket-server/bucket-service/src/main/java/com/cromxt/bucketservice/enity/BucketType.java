package com.cromxt.bucketservice.enity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BucketType {
    SMALL(5120L),
    MEDIUM(8192L),
    LARGE(10240L);
    private final Long bucketSize;
}
