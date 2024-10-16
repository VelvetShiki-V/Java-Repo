package com.vs.cloud_model.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {
    // 可改造为远程配置文件注入
    private static final String ELASTICSEARCH_HOST = "127.0.0.1";
    private static final int ELASTICSEARCH_PORT = 9200;
    private static final String ELASTICSEARCH_SCHEME = "http";

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(ELASTICSEARCH_HOST, ELASTICSEARCH_PORT, ELASTICSEARCH_SCHEME)
                )
        );
    }
}
