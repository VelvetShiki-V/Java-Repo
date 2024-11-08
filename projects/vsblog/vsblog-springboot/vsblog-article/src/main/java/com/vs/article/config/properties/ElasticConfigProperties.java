package com.vs.article.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "profile.search.elasticsearch")
public class ElasticConfigProperties {

    private String host;

    private Integer port;

    private String scheme;

}
