package com.cromxt.storageserver.service.impl;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@Getter
public class BucketInformationService {

    private final Integer httpPort;
    private final Integer rpcPort;
    private final String bucketId;
    private final String applicationHostname;
    private final String baseDirectory;
    private final String rootDirectory;
    private final String routeIp;

    public BucketInformationService(
            Environment environment
    ) {
        this.rootDirectory = environment.getProperty("STORAGE_SERVER_DISK_PATH", String.class);
        this.baseDirectory = rootDirectory + "/" + "stored-data";
        this.rpcPort = environment.getProperty("STORAGE_SERVER_GRPC_SERVICE_PORT", Integer.class);
        this.httpPort = environment.getProperty("STORAGE_SERVER_HTTP_SERVICE_PORT", Integer.class);
        this.bucketId = environment.getProperty("STORAGE_SERVER_ID", String.class);
        String hostName = findHostName();
        this.applicationHostname = hostName;
        String profile = environment.getProperty("spring.profiles.active", String.class);
        assert profile != null;
        this.routeIp = getRouteIp(profile, hostName);
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

    private String getRouteIp(String profile, String applicationHostname) {
        return switch (profile) {
            case "local", "local-docker", "crombucket", "crombucket-docker" -> applicationHostname;
            default -> "localhost";
        };
    }
}
