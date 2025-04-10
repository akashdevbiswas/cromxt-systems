package com.cromxt.userservice.sdk;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FileVisibility {
    PUBLIC("pub"),
    PRIVATE("prv"),
    PROTECTED("prt");

    private final String accessType;

}
