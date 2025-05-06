package com.crombucket.common.mediaservice.requests;


import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaRequest {
    private String clientId;
    private String bucketId;
    private String clusterId;
    private String regionId;
    private String visibility;
}
