package com.crombucket.common.mediaservice.requests;

import com.crombucket.common.ObjectsVisibility;

public record MediaObjectInitializeRequest(
    String originalfilename,
    String prefferedFileName,
    Long fileSize,
    ObjectsVisibility visibility
) {
}
