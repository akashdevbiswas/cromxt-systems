package com.crombucket.storagemanager.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
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
    private String storageCode;
    @DocumentReference
    private Clusters clusterId;

    private LocalDate initDate;
}
