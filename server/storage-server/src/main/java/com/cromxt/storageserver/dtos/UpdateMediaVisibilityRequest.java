package com.cromxt.storageserver.dtos;

public record UpdateMediaVisibilityRequest(
        String mediaId,
        String visibility
){

}
