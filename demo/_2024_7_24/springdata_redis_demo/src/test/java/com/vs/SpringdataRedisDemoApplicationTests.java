package com.vs;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.vs.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SpringdataRedisDemoApplicationTests {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    // 经过配置，使用泛型构造的RedisTemplate工厂bean对象都会经过自动序列化与反序列化操作，并以json串方式流通了
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void redisTestString() {
        stringRedisTemplate.opsForValue().set("name", "velvetShiki", 10, TimeUnit.SECONDS);
        Object name = stringRedisTemplate.opsForValue().get("name");
        System.out.println(name);
    }

    @Test
    void redisTestObject() {
        User user = User.of(0, "shiki", "zcx123");
        User user2 = User.of(1, "velvet", "123zcx");
        redisTemplate.opsForValue().set("user:1", user);
        redisTemplate.opsForValue().set("user:2", user2);
        User obj = (User) redisTemplate.opsForValue().get("user:2");
        System.out.println(obj);
    }

    @Test
    void redisTestObject2() {
        User user = User.of(2, "VS", "zcx12333");
        // 使用stringRedisTemplate处理对象val前需要手动序列化
        String s = JSONObject.toJSONString(user);
        stringRedisTemplate.opsForValue().set("user:3", s);
        // get时获取到的是redis中存储val的json串，需要手动反序列化回到指定pojo类型
        User obj = JSON.parseObject(stringRedisTemplate.opsForValue().get("user:3"), User.class);
        System.out.println(obj);
    }

    @Test
    void redisTestHash() {
        stringRedisTemplate.opsForHash().put("article:1", "breaking news", "mayday");
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries("article:1");
        System.out.println(entries);
    }
}
