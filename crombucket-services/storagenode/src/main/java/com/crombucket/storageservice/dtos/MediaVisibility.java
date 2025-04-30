package com.crombucket.storageservice.dtos;

import com.crombucket.storageservice.constants.FileVisibility;

public record MediaVisibility(
        FileVisibility visibility
) {
}
