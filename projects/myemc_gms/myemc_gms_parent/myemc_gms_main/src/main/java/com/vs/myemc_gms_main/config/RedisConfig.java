package com.vs.myemc_gms_main.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.vs.utils.RedisUtil;

@Configuration
public class RedisConfig {
    @Autowired
    RedisConfig(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    private final StringRedisTemplate stringRedisTemplate;

    // 创建redisTemplate实例并注入工具类
    @PostConstruct
    public void initTemplate() {
        RedisUtil.setStringRedisTemplate(stringRedisTemplate);
    }
}
