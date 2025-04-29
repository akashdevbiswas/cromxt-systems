package com.crombucket.storagemanager.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StorageNode {

    @Id
    private String id;
    @Indexed(unique = true)
    private String storageCode;
    @JsonManagedReference
    @DocumentReference(lookup = "{ 'clusterCode' : ?#{#target} }")
    private StorageClusters clusters;
    private LocalDate createdAt;
    private LocalDate clusterJoinedDate;
    private Long availableSpace;
}

