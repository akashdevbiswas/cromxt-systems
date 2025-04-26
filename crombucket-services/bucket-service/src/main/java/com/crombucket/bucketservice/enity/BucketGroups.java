package com.crombucket.bucketservice.enity;


import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Document(collection = "buckets-group")
public class BucketGroups {
    @Id
    private String id;
    private String bucketGroupName;
    private String clientId;
    private Long availableBytes;
    private Long totalSpace;
    @DBRef
    private Set<BucketGroups> bucketGroups;
}
