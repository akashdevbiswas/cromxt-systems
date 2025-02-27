package com.cromxt.bucket.models;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MediaObjects {
    private String mediaId;
    private Long fileSize;
    private String contentType;
    private String accessUrl;
}
