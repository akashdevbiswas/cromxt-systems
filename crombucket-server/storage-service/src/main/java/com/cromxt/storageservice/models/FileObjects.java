package com.cromxt.storageservice.models;


import com.cromxt.storageservice.constants.FileVisibility;
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
