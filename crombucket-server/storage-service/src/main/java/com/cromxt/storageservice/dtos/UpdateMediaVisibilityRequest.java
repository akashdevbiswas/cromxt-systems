package com.cromxt.storageservice.dtos;

public record UpdateMediaVisibilityRequest(
        String mediaId,
        String visibility
){

}
