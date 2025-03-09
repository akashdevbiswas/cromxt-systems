package com.cromxt.bucket.models;


import com.cromxt.bucket.constants.FileVisibility;
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
    private FileVisibility visibility;
}
