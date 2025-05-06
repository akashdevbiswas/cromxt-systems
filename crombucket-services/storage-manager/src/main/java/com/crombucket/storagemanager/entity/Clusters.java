package com.crombucket.storagemanager.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

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
    private LocalDate createdOn;
}
