package com.cromxt.clusterrouter.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Buckets {
    private String bucketId;
    private String hostName;
    private Integer rpcPort;
    private Integer httpPort;
    private Long lastRefreshTime;
    private Long availableSpaceInBytes;
}