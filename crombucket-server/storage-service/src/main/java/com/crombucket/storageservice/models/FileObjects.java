package com.crombucket.storageservice.models;


import com.crombucket.storageservice.constants.FileVisibility;
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
