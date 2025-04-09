package com.cromxt.clusterrouter.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StorageServerResponse {
    private String storageServerId;
    private String hostName;
    private Integer rpcPort;
    private Long lastRefreshTime;
    private Long availableSpaceInBytes;
}
