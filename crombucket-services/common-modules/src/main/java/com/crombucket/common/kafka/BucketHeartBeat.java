package com.crombucket.common.kafka;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BucketHeartBeat {

    private String bucketId;
    private Long availableSpaceInBytes;
}
