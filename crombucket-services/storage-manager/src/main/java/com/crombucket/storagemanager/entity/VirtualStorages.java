package com.crombucket.storagemanager.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Document
@Builder
@AllArgsConstructor
@Getter
@Setter
public class VirtualStorages {
  private String id;
  private MemoryType memoryType;
  @DBRef
  private Clusters clusters;
  @DBRef
  private Bucket bucketId;
  @CreatedDate
  private LocalDate createdAt;
}
