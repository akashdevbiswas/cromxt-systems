package com.cromxt.common.crombucket.bucketmanager;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClusterInformation {
    private String clusterRouterUrl;
    private String clusterName;
    private String clusterId;
}
