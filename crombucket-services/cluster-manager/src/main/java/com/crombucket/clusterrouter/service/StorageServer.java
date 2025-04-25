package com.crombucket.clusterrouter.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageServer {
    private String id;
    private String hostName;
    private Integer rpcPort;
    private Long lastRefreshTime;
    private Long availableSpaceInBytes;
}