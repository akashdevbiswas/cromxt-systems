package com.cromxt.common.crombucket.bucketmanager;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UsersBucketInfo {
    private Long availableSpace;
}
