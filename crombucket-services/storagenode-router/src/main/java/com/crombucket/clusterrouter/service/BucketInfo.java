package com.crombucket.clusterrouter.service;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BucketInfo {
    private String bucketId;
    private Long availableSpaceInBytes;
}
