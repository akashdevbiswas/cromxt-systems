package com.crombucket.mediaservice.entity;


import lombok.*;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.crombucket.common.MediaUploadStatus;
import com.crombucket.common.ObjectsVisibility;

@Document(collection = "media_objects")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Medias {
    @Id
    private String mediaId;
    @Indexed(unique = true)
    private String fileId;
    private String preffredFileName;
    private Long fileSize;
    private String bucketId;
    private String fileExtension;
    private ObjectsVisibility visibility;
    private MediaUploadStatus uploadStatus;
    @CreatedDate
    private LocalDate createdOn;
    private LocalDate updatedOn;
    private String storageNodeId;
    private String clusterId;
}
