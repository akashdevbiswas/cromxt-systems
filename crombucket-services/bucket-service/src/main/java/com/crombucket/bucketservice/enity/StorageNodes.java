package com.crombucket.bucketservice.enity;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StorageNodes {

    @Id
    private String id;
    private String storageCode;
    @DocumentReference
    private Clusters clusterId;
    
    private LocalDate initDate;
}
