package com.crombucket.storagemanager.entity;


import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
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
public class Clusters {
    @Id
    private String id;
    @Indexed(unique = true)
    private String clusterCode;
    private Long capacity;
    private Long availableSpace;
    private LocalDate createdOn;
    @DocumentReference
    private Regions region;
}
