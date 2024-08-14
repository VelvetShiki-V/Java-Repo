package com.vs.utils;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.vs.common.GlobalConstants.*;

@Slf4j
public class RedisUtil {
    // static instance
    private static StringRedisTemplate stringRedisTemplate;
    private static RedissonClient redissonClient;

    // 静态变量依赖注入初始化
    @Autowired
    public RedisUtil(RedissonClient client) {
        redissonClient = client;
    }

//     初始化template
    public static void setStringRedisTemplate(StringRedisTemplate template) {
        stringRedisTemplate = template;
    }

    // 内部类存储逻辑对象
    @Data
    public static class RedisData {
        private Object object;
        private LocalDateTime expireTime;
    }

    // 普通数据存储(对象序列化)
    public static void set(String key, Object value) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value));
    }

    // 普通数据存储(对象序列化 + TTL)
    public static void setWithTTL(String key, Object value, Long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), timeout, unit);
    }

    // 热点数据存储(逻辑过期 + 对象装饰器序列化 -> 缓存击穿)
    public static void setWithLogicExpire(String key, Object value, Long timeout, TimeUnit unit) {
        // 对象转换
        RedisData redisData = new RedisData();
        redisData.setObject(value);
        // 设置逻辑过期时间
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(timeout)));
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    // 普通数据获取(string类型)
    public static String query(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // 普通数据获取(数据TTL重置 + 缓存穿透空值处理 + 数据反序列化为指定类型)
    // 注意dbFallBack参数需要的是一个Function<ID, R>类型的函数，而不是函数执行后的结果
    public static <R> R queryTTLWithDB(String key, Class<R> objType, Long timeout, TimeUnit unit, Function<Object[], R> dbFallBack, Object...args) {
        // 查询数据(json串)是否存在于缓存中
        String jsonStr = stringRedisTemplate.opsForValue().get(key);
        // isNotBlank为true仅当jsonStr 既不为 null，也不为 ""
        if(StrUtil.isNotBlank(jsonStr)) return JSONUtil.toBean(jsonStr, objType);
        // 取出为空值，则该key为空缓存
        else if(Objects.equals(jsonStr, "")) return null;
        // 不存在，到数据库中查找数据
        else {
            R ret = dbFallBack.apply(args);
            if(ret == null) {
                // 数据库数据不存在，缓存穿透空值处理
                log.error("数据库数据不存在，空值缓存");
                stringRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
            } else {
                // 数据存在，序列化缓存到redis中, 并将结果返回
                log.info("数据库已查到, 刷新缓存");
                setWithTTL(key, ret, timeout, unit);
                return ret;
            }
        }
        return null;
    }

    // 热点数据获取(数据永不过期 + 逻辑过期预防缓存击穿 + 加锁多线程异步数据获取)
    public static <ID, R> R queryWithLogicExpire(String key, ID Id, Class<R> objType, Function<ID, R> dbFallBack, Long timeout, TimeUnit unit) throws InterruptedException {
        // 缓存查询
        String jsonStr = stringRedisTemplate.opsForValue().get(key);
        // 热点数据不存在，直接返回null
        if(StrUtil.isBlank(jsonStr)) return null;
        else {
            // 命中数据，对比逻辑时间判断是否过期
            RedisData redisData = JSONUtil.toBean(jsonStr, RedisData.class);
            R obj = JSONUtil.toBean((JSONObject) redisData.getObject(), objType);
            LocalDateTime recordTime = redisData.getExpireTime();
            if(LocalDateTime.now().isAfter(recordTime)) {
                // 数据已过期, 互斥锁 + 开启线程到数据库中获取最新数据
                RLock lock = redissonClient.getLock(key + Id);
                if(lock.tryLock(10, TimeUnit.SECONDS)) {
                    try {
                        threadPools.submit(() -> {
                            // 开启异步线程获取数据库数据
                            R data = dbFallBack.apply(Id);
                            if(data != null) {
                                // 热更新到缓存，并刷新逻辑过期时间
                                setWithLogicExpire(key, data, timeout, unit);
                            } else {
                                // 数据不存在，删除缓存
                                stringRedisTemplate.delete(key);
                            }
                            // 将数据返回，返回前将锁释放
                            return data;
                        });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        lock.unlock();
                    }
                }
            } else {
                // 未过期，直接返回数据
                return obj;
            }
        }
        return null;
    }

    // 自增统计量，key结构: 实时时间戳 + redis自增计数器
    public static Long recordDailyIncrement(String key) {
        // 使用每日Ts(formatted)作为key并使值自增以统计每日新增数量
        String curDay = TimeUtil.getCurrentDay();
        return stringRedisTemplate.opsForValue().increment(INCREMENT_HEADER + key + curDay);
    }

    // 雪花算法全局唯一ID生成
    public static long uniKeyGen (String keyPrefix) {
        long increment = recordDailyIncrement(keyPrefix);
        // 雪花算法生成唯一ID
        long Ts = TimeUtil.getCurrentTs();
        return Ts << LEFT_MOVE_BITS | increment;
    }

    // redisson锁控制
    public static <R> R taskLock(Function<Object[], R> fallBack, String key, Object...args) {
        RLock lock = redissonClient.getLock(key);
        boolean isLocked = false;
        R ret = null;
        try {
            isLocked = lock.tryLock(REDISSON_LOCK_WAIT_MAX_SECONDS, TimeUnit.SECONDS);
            if(isLocked) ret = fallBack.apply(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(isLocked) lock.unlock();
            log.info("RLock锁已释放");
        }
        return ret;
    }

    // 删除某个key
    public static Boolean removeKey(String key) {
        return stringRedisTemplate.delete(key);
    }

    // 自定义分布式锁
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
