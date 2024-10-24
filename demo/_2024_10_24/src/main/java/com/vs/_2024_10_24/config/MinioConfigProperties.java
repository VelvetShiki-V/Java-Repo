package com.vs._2024_10_24.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioConfigProperties {
    public String endpoint;
    public String accessKey;
    public String secretKey;
    public String bucket;
}
