package com.cromxt.storageservice.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileVisibility {
    PUBLIC("pub"),
    PRIVATE("prv"),
    PROTECTED("prt");

    private final String accessType;

}
