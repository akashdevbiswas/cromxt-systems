package com.crombucket.storagemanager.entity;

import lombok.Getter;

@Getter
public enum MemoryType {
  LARGE(10737418240L),
  MEDIUM(5368709120L),
  SMALL(2147483648L);

  private final Long capacityInBytes;

  MemoryType(Long capacity) {
    this.capacityInBytes = capacity;
  }
}
