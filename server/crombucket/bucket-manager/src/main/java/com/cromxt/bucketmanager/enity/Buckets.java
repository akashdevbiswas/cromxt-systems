package com.cromxt.bucketmanager.enity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document
public class Buckets {

    @Id
    private String id;
    private String clientId;
    private BucketType bucketType;
    private Long availableSpace;
    @DocumentReference
    private BucketGroups bucketGroups;
}
