package com.vs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class jedisTestTest {
    private Jedis jedis;

    @BeforeEach
    void init() {
        jedis = new Jedis("1.94.180.58", 6379);
        jedis.auth("666666");
        jedis.select(0);
    }

    @Test
    void testString() {
        String ret = jedis.set("new_name", "nihao");
        System.out.println(ret);
    }

    @Test
    void testHash() {
        jedis.hset("photo:1", "url", "www.test.com");
        jedis.hset("photo:2", "url", "www.test2.com");

        Map<String, String> ret = jedis.hgetAll("photo:1");
        Map<String, String> ret2 = jedis.hgetAll("photo:2");

        System.out.println(ret);
        System.out.println(ret2);
    }

    @AfterEach
    void destruct() {
        if(jedis != null) jedis.close();
    }
}