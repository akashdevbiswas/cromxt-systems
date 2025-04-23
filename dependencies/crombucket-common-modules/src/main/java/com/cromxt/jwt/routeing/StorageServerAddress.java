package com.cromxt.jwt.routeing;


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
