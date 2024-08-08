package com.vs.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RedisUtilTest {

    @Test
    void uniKeyGen() {
        System.out.println(TimeUtil.getCurrentTs());
    }
}