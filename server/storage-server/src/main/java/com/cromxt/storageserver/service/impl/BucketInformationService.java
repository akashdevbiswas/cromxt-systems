package com.cromxt.storageserver.service.impl;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@Service
@Getter
public class BucketInformationService {

    private final Integer httpPort;
    private final Integer rpcPort;
    private final String bucketId;
    private final String applicationHostname;
    private final String baseDirectory;

    public BucketInformationService(
            Environment environment
    ) {
        this.baseDirectory = environment.getProperty("STORAGE_SERVER_DISK_PATH", String.class);
        this.rpcPort = environment.getProperty("STORAGE_SERVER_GRPC_SERVICE_PORT", Integer.class);
        this.httpPort = environment.getProperty("STORAGE_SERVER_HTTP_SERVICE_PORT", Integer.class);
        this.bucketId = environment.getProperty("STORAGE_SERVER_ID", String.class);
        Boolean findHostIp = environment.getProperty("STORAGE_SERVER_FIND_HOST_IP", Boolean.class, false);
        if (findHostIp) {
            this.applicationHostname = findHostName();
        } else {
            this.applicationHostname = "127.0.0.1";
        }

    }

    private String findHostName() {
        String hostName;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            hostName = ip.getHostAddress();
        } catch (UnknownHostException e) {
            log.error(e.getMessage(), e);
            hostName = "localhost";
            log.warn("Bucket starts with {}", "localhost");
        }
        return hostName;
    }

    public Long getAvailableSpace() {
        File rootDirectory = new File(baseDirectory);
        return rootDirectory.getFreeSpace();
    }
}
