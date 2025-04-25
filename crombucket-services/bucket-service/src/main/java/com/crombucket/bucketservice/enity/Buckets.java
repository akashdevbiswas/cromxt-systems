package com.crombucket.bucketservice.enity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Buckets {

    @Id
    private String id;
    private String clientId;
    private BucketType bucketType;
    private Long availableSpace;
    @DocumentReference
    private BucketGroups bucketGroups;
}
