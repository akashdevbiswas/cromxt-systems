package com.crombucket.storageservice.dtos;

public record UpdateMediaVisibilityRequest(
        String mediaId,
        String visibility
){

}
