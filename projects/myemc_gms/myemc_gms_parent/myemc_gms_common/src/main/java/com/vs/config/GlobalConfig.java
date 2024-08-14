package com.vs.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// 用于读取配置文件常量
@Data
@Configuration
@ConfigurationProperties(prefix = "global-dynamic-constants")
public class GlobalConfig {
    // FIXME: 配置文件常量无法读取
    // 动态配置常量, 注意命名应该与yml中的key对应
    private int thread_pool_max_size;
    private Long redis_cache_max_ttl_minutes;
    private Long redisson_lock_wait_max_seconds;
    private Long jwt_expire_duration_seconds;
}
