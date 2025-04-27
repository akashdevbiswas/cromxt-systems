package com.crombucket.storagemanager.entity;


import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
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
public class Clusters {

    @Id
    private String id;
    private String clusterName;
    private Long capacity;
    @DBRef
    private Set<StorageNode> storageNodes;
}
