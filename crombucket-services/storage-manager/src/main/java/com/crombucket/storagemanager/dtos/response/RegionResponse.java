package com.crombucket.storagemanager.dtos.response;

import java.time.LocalDate;

public record RegionResponse(
  String regionName,
  String regionCode,
  LocalDate startedFrom
) {
  
}
