package com.cromxt.storageserver.models;


import com.cromxt.storageserver.constants.FileVisibility;
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
