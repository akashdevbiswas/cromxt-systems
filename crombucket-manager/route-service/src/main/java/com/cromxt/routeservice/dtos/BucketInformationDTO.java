package com.cromxt.routeservice.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BucketInformationDTO {
    private String bucketId;
    private String hostName;
    private Integer rpcPort;
    private Integer httpPort;
    private Long lastRefreshTime;
    private Long availableSpaceInBytes;
}
