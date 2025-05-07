package com.crombucket.storagemanager.dtos.requests;

import java.util.List;

public record BucketRequest(
  String bucketName,
  List<VirtualStorageRequest> virtualStorages
) {
  
}
