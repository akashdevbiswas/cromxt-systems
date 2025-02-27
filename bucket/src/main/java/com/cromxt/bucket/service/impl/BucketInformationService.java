package com.cromxt.bucket.service.impl;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@Service
@Getter
public class BucketInformationService {

    @Getter(AccessLevel.NONE)
    private final Boolean applicationHostnameAutoDiscovery;
    private final Integer httpPort;
    private final Integer rpcPort;
    private final String bucketId;


    public BucketInformationService(
            Environment environment
    ) {
        this.applicationHostnameAutoDiscovery = environment.getProperty("BUCKET_CONFIG_MACHINE_IP_AUTO_DISCOVERY", Boolean.class);
        this.rpcPort = environment.getProperty("BUCKET_CONFIG_GRPC_SERVICE_PORT", Integer.class);
        this.httpPort = environment.getProperty("BUCKET_CONFIG_HTTP_SERVICE_PORT", Integer.class);
        this.bucketId = environment.getProperty("BUCKET_CONFIG_ID",String.class);

    }

    public String getApplicationHostname() {
        String hostName;
        if(applicationHostnameAutoDiscovery) {
            try {
                InetAddress ip = InetAddress.getLocalHost();
                hostName = ip.getHostAddress();
            } catch (UnknownHostException e) {
                log.error(e.getMessage(), e);
                hostName = "localhost";
                log.warn("Bucket starts with {}", "localhost");
            }
        }else{
            hostName = "localhost";
        }
        return hostName;
    }

}
