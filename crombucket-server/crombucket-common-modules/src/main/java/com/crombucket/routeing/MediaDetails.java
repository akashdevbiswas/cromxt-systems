package com.cromxt.common.crombucket.routeing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MediaDetails {
    Long fileSize;
    String fileExtension;
}