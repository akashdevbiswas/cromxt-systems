package com.crombucket.storagemanager.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Document
@Builder
@Data
@AllArgsConstructor
public class Bucket {

  @Id
  private String id;
  private String name;
  private String accessKey;
  private String secretKey;
  private String regionId;
}
