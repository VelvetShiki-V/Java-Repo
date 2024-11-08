package com.vs.article.config;

import com.vs.article.config.properties.ElasticConfigProperties;
import jakarta.annotation.Resource;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    @Resource
    ElasticConfigProperties properties;

    @Bean
    public RestHighLevelClient restHighLevelClientInit() {
        return new RestHighLevelClient(RestClient.builder(
                new HttpHost(
                        properties.getHost(),
                        properties.getPort(),
                        properties.getScheme())
        ));
    }
}
