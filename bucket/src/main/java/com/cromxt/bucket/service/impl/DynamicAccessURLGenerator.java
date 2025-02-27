package com.cromxt.bucket.service.impl;


import com.cromxt.bucket.service.AccessURLGenerator;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Profile({"dev","prod"})
public class DynamicAccessURLGenerator implements AccessURLGenerator {
    private final String clusterId;
    private final String zoneId;

    public DynamicAccessURLGenerator(Environment environment) {
        String clusterId = environment.getProperty("BUCKET_CONFIG_CLUSTER_ID", String.class);
        String zoneId = environment.getProperty("BUCKET_CONFIG_ZONE_ID", String.class);
        assert clusterId != null && zoneId != null;
        this.clusterId = clusterId;
        this.zoneId = zoneId;
    }

    @Override
    public String generateAccessURL(String mediaId) {
        return "";
    }
}
