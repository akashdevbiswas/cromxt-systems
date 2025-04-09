package com.cromxt.storageserver.dtos;

import com.cromxt.storageserver.constants.FileVisibility;

public record MediaVisibility(
        FileVisibility visibility
) {
}
