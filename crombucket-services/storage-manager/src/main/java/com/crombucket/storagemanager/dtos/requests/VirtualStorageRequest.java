package com.crombucket.storagemanager.dtos.requests;

import java.lang.management.MemoryType;

public record VirtualStorageRequest(
    MemoryType memoryType,
    Integer numberOfStorages) {

}
