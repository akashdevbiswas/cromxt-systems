package com.crombucket.storagemanager.entity;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Document
@Builder
@AllArgsConstructor
public class VirtualStorages {
  private String id;
  private String name;
  @DBRef
  private Clusters clusters;
  @DBRef
  private Bucket parentId;
}
