package com.cromxt.common.crombucket.routeing;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class StorageServerAddress {
        String hostName;
        Integer rpcPort;
}
