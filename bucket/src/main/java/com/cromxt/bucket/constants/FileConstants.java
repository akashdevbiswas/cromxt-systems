package com.cromxt.bucket.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileConstants {
    PUBLIC_ACCESS("189612768"),
    PRIVATE_ACCESS("85109297");

    private final String accessKey;

}
