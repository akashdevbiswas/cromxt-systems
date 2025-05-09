package com.crombucket.storagemanager.dtos.response;

import java.util.Date;

public record RegionResponse(
  String regionName,
  String regionCode,
  Date startedFrom
) {
  
}
