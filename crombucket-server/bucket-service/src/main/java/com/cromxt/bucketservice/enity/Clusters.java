package com.cromxt.bucketservice.enity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Clusters {

    @Id
    private String id;
    private String clusterName;
    private Long availableSpace;
}
