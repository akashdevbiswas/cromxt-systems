package com.crombucket.bucketservice.enity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Buckets {
    @Id
    private String id;
    private String clientId;
    private Long capacity; 
}
