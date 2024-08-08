package com.vs.myemc_gms_main.config;

import com.vs.utils.RedisUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostConstruct
    public void initTemplate() {
        RedisUtil.setStringRedisTemplate(stringRedisTemplate);
    }
}
