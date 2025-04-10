package com.cromxt.userservice.sdk;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateFileVisibilityRequest {
    private String mediaId;
    private FileVisibility visibility;
}
