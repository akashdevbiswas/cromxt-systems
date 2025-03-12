package com.cromxt.bucketmanager.dtos.response;


import com.cromxt.bucketmanager.enity.BucketType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BucketResponse {
    private String bucketId;
    private BucketType bucketType;
    private String ownerId;
    private LocalDate creationDate;
    private Long availableSpace;
}
