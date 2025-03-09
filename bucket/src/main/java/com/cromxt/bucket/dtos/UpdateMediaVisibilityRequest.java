package com.cromxt.bucket.dtos;

public record UpdateMediaVisibilityRequest(
        String mediaId,
        String fileVisibility
){

}
