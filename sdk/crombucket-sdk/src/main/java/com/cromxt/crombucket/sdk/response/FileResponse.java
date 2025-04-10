package com.cromxt.crombucket.sdk.response;


import com.cromxt.crombucket.sdk.FileVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FileResponse {
    private String mediaId;
    private String accessUrl;
    private Long fileSize;
    private FileVisibility visibility;
}
