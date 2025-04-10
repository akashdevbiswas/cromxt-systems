package com.cromxt.crombucket.sdk;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateFileVisibilityRequest {
    private String mediaId;
    private FileVisibility visibility;
}
