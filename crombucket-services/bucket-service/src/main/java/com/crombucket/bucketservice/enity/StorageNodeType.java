package com.crombucket.bucketservice.enity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StorageNodeType {
    SMALL(5120L),
    MEDIUM(8192L),
    LARGE(10240L);
    private final Long bucketSize;
}
