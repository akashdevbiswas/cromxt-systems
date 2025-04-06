package com.cromxt.common.crombucket.systemmanager;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class StorageServerRequest {
    private String storageServerId;
    private String hostName;
    private Integer rpcPort;
}
