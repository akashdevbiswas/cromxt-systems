package com.cromxt.common.crombucket.mediamanager.response;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class MediaObjects {
    private String mediaId;
    private String fileId;
    private Long fileSize;
    private String accessUrl;
    private String createdOn;
    private String visibility;
}
