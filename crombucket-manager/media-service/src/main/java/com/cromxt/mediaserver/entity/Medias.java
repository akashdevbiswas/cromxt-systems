package com.cromxt.mediaserver.entity;


import com.cromxt.common.crombucket.dtos.mediaserver.requests.MediaStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "media_objects")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Medias {

    @Id
    private String id;
    @Indexed(unique = true)
    private String mediaName;
    private String userId;
    private Long size;
    private String bucketId;
    private String fileExtension;
    private MediaStatus mediaStatus;
}
