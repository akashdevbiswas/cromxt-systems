package com.cromxt.bucket.models;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class FileObjects {
    private String fileId;
    private Long fileSize;
    private String extension;
    private String absolutePath;
    private Boolean isPublic;
}
