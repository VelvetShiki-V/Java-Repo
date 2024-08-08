package com.vs.utils;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

public class RedisUtil {
    private static final int LEFT_MOVE_BITS = 32;
    private static StringRedisTemplate stringRedisTemplate;

    // 初始化template
    public static void setStringRedisTemplate(StringRedisTemplate template) {
        stringRedisTemplate = template;
    }

    // 全局唯一ID生成(redis键自增 + 雪花算法策略)
    // 用途：每日唯一key，方便统计数量，key结构: 实时时间戳 + redis自增计数器
    public static long uniKeyGen (String keyPrefix) {
        // 使用每日Ts(formatted)作为key并使值自增以统计每日新增数量
        String curDay = TimeUtil.getCurrentDay();
        long increment = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + curDay);
        // 雪花算法生成唯一ID
        long Ts = TimeUtil.getCurrentTs();
        return Ts << LEFT_MOVE_BITS | increment;
    }

    // 存储key
    public static void setValue(String key, String value, Long time, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, value, time, unit);
    }

    // 获取val
    public static String getValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // TODO: 缓存穿透util
    public static void queryWithPassThrough(String key) {}

    // TODO: 缓存击穿util

    // TODO: 乐观锁

}
