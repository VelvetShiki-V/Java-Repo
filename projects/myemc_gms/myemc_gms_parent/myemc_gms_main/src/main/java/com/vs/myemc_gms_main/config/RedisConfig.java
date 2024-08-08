package com.vs.myemc_gms_main.config;

import com.vs.utils.RedisUtil;
import jakarta.annotation.PostConstruct;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 创建redisTemplate实例并注入工具类
    @PostConstruct
    public void initTemplate() {
        RedisUtil.setStringRedisTemplate(stringRedisTemplate);
    }
}
