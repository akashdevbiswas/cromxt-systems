package com.cromxt.bucket.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileConstants {
    PUBLIC_ACCESS("pub"),
    PRIVATE_ACCESS("prv"),
    PROTECTED_ACCESS("prt");

    private final String accessType;

}
