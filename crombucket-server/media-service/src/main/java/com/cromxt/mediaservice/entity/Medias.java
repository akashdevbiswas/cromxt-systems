package com.cromxt.mediaservice.entity;


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
    private String mediaId;
    @Indexed(unique = true)
    private String fileId;
    private String clientId;
    private Long fileSize;
    private String bucketId;
    private String fileExtension;
}
