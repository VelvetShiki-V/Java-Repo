package com.vs.utils;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class RedisUtil {
    // params
    private static final int LEFT_MOVE_BITS = 32;
    private static final String INCREMENT_HEADER = "icr:";
    private static final String LOCK_HEADER = "dbLock:";
    private static final Long CACHE_NULL_TTL = 30L;

    // instance
    private static StringRedisTemplate stringRedisTemplate;
    private static final ExecutorService threadPools = Executors.newFixedThreadPool(10);

    // 初始化template
    public static void setStringRedisTemplate(StringRedisTemplate template) {
        stringRedisTemplate = template;
    }

    // 内部类
    @Data
    public static class RedisData {
        private Object object;
        private LocalDateTime expireTime;
    }

    // 全局唯一ID生成(redis键自增 + 雪花算法策略)
    // 用途：每日唯一key，方便统计数量，key结构: 实时时间戳 + redis自增计数器
    public static long uniKeyGen (String keyPrefix) {
        // 使用每日Ts(formatted)作为key并使值自增以统计每日新增数量
        String curDay = TimeUtil.getCurrentDay();
        long increment = stringRedisTemplate.opsForValue().increment(INCREMENT_HEADER + keyPrefix + curDay);
        // 雪花算法生成唯一ID
        long Ts = TimeUtil.getCurrentTs();
        return Ts << LEFT_MOVE_BITS | increment;
    }

    // 普通数据存储(接收任意对象类型数据)
    public static void setValue(String key, Object value, Long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), timeout, unit);
    }

    // 热点数据存储(逻辑过期 + 装饰器处理缓存击穿)
    public static void setWithLogicExpire(String key, Object value, Long timeout, TimeUnit unit) {
        // 对象转换
        RedisData redisData = new RedisData();
        redisData.setObject(value);
        // 设置逻辑过期时间
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(timeout)));
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    // 普通数据获取(缓存穿透预防 + 将数据反序列化为指定类型)
    public static <ID, R> R queryWithPassThrough(String key, ID Id, Class<R> objType, Function<ID, R> dbFallBack, Long timeout, TimeUnit unit) {
        // 查询数据(json串)是否存在于缓存中
        String jsonStr = stringRedisTemplate.opsForValue().get(key);
        // isNotBlank为true仅当jsonStr 既不为 null，也不为 ""
        if(StrUtil.isNotBlank(jsonStr)) return JSONUtil.toBean(jsonStr, objType);
        else if(Objects.equals(jsonStr, "")) return null;
        // 不存在，开启新线程到数据库中查找数据
        else {
            R ret = dbFallBack.apply(Id);
            if(ret == null) {
                // 数据库数据不存在，缓存控制防止缓存穿透
                stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
            } else {
                // 数据存在，序列化缓存到redis中, 并将结果返回
                setValue(key, ret, timeout, unit);
                return ret;
            }
        }
        return null;
    }

    // 热点数据获取(缓存击穿预防 + 逻辑过期 + 多线程异步同步数据)
    public static <ID, R> R queryWithLogicExpire(String key, ID Id, Class<R> objType, Function<ID, R> dbFallBack, Long timeout, TimeUnit unit) {
        // 缓存查询
        String jsonStr = stringRedisTemplate.opsForValue().get(key);
        // 热点数据不存在，直接返回null
        if(StrUtil.isBlank(jsonStr)) return null;
        else {
            // 否则命中数据，对比逻辑时间判断是否过期
            RedisData redisData = JSONUtil.toBean(jsonStr, RedisData.class);
            R obj = JSONUtil.toBean((JSONObject) redisData.getObject(), objType);
            LocalDateTime recordTime = redisData.getExpireTime();
            if(LocalDateTime.now().isAfter(recordTime)) {
                // 数据已过期, 开启线程到数据库中获取最新数据
                // TODO
            } else {
                // 未过期，直接返回数据
                return obj;
            }
        }
        return null;
    }

    // 分布式锁
    // uuid唯一key(dbLock:rm:model:Id:xxxx:UUID:xxxx)
    public static String dbLockKeyGen(String keyPrefix) {
        return LOCK_HEADER + keyPrefix + ":UUID:" + UUID.randomUUID(true);
    }

    // 锁创建
    public static boolean distributedTryLock(String key, long timeOut, TimeUnit unit) {
        // set key val NX EX timeout
        // 获取线程id, 用于锁设置
        long threadId = Thread.currentThread().getId();
        Boolean ret = stringRedisTemplate
                .opsForValue()
                .setIfAbsent(key, String.valueOf(threadId), timeOut, unit);
        return BooleanUtil.isTrue(ret);     // 包装类判断拆箱返回
    }

    // 释放锁(必须是原子性操作)
    public static void distributedUnlock(String key) {
        stringRedisTemplate.delete(key);
    }
}
