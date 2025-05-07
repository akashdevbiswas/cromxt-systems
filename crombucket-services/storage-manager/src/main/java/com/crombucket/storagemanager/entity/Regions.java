package com.crombucket.storagemanager.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;



@Document
@AllArgsConstructor
@Builder
@Data
public class Regions {
  @Id
  private String id;
  @Indexed(unique = true)
  private String regionCode;
  @Indexed(unique = true)
  private String regionName;
  @CreatedDate
  private LocalDate startedOn;
}
