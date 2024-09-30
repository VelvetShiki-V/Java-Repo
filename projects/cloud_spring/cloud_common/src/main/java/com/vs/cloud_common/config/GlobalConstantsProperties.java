package com.vs.cloud_common.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "global-constants")
public class GlobalConstantsProperties {
    private String SYSTEM_REDIS_KEY_PREFIX;
    private String SECRET_KEY_PREFIX;
    private Long SECRET_KEY_EXPIRE_MILLISECONDS;
    private int THREAD_POOL_SIZE_MAX;
    private String CACHE_NULL;
    private Long CACHE_NULL_TTL_MINUTES;
    private Long REDIS_CACHE_MAX_TTL_MINUTES;
    private Long JWT_EXPIRE_DURATION_MINUTES;
    private String JWT_PREFIX;
    private String QUERY_USER_ALL;
    private String QUERY_USER_PREFIX;
    private String REMOVE_USER_PREFIX;
    private String UPDATE_USER_PREFIX;
    private String QUERY_MODEL_ALL;
    private String QUERY_MODEL_PREFIX;
    private String REMOVE_MODEL_PREFIX;
    private String UPDATE_MODEL_PREFIX;
    private int REDISSON_LOCK_WAIT_MAX_SECONDS;

    @PostConstruct
    public void init() {
//        System.out.println("初始化观察: " + SYSTEM_REDIS_KEY_PREFIX);
//        System.out.println("初始化观察: " + SECRET_KEY_PREFIX);
    }
}
