package com.cromxt.bucket.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileVisibility {
    PUBLIC_ACCESS("pub"),
    PRIVATE_ACCESS("prv"),
    PROTECTED_ACCESS("prt");

    private final String accessType;

}
