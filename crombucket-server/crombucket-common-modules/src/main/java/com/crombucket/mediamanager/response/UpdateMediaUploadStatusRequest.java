package com.cromxt.common.crombucket.mediamanager.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateMediaUploadStatusRequest{
        private Long fileSize;
        private String fileId;
        private String visibility;
        private String accessUrl;
}
