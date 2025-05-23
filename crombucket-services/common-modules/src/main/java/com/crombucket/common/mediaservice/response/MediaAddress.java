package com.crombucket.common.mediaservice.response;



public record MediaAddress(
    String url,
    Integer port,
    String mediaId
) {
    
}
