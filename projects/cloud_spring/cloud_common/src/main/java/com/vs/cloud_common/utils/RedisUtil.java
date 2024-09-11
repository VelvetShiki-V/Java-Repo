package com.vs.cloud_common.utils;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import static com.vs.cloud_common.constants.GlobalConstants.*;

@Slf4j
@Component
public class RedisUtil {
    // 数据存储
    public static <T> void set(StringRedisTemplate template, String key, T value, Long timeout, TimeUnit unit) {
        if(value instanceof String) template.opsForValue().set(key, (String) value, timeout, unit);
        else template.opsForValue().set(key, JSONUtil.toJsonStr(value), timeout, unit);
    }

    // 数据获取
    public static <R> Object query(StringRedisTemplate template, String key, TypeReference<R> typeRef) {
        String str = template.opsForValue().get(key);
        if(StrUtil.isNotBlank(str)) {
            // 缓存中存在数据
            log.info("{} 缓存命中", key);
            if (typeRef.getType() == String.class) return str;
            else return JSONUtil.toBean(str, typeRef.getType(), true);
        } else if(CACHE_NULL.equals(str)) return CACHE_NULL;
        log.warn("{} 缓存不存在", key);
        return null;
    }

    // 数据获取(缓存TTL重置 + 缓存穿透空值处理)
    // 注意dbFallBack参数需要的是一个Function<ID, R>类型的函数，而不是函数执行后的结果
    public static <R> R queryTTLWithDB(StringRedisTemplate template, String key, TypeReference<R> targetTypeRef,
                                       Long timeout, TimeUnit unit, Function<Object[], Object> dbFallBack,
                                       Object...args) {
        // 查询数据(json串)是否存在于缓存中
        String str = template.opsForValue().get(key);
        if(StrUtil.isNotBlank(str)) {
            log.info("{} 缓存命中", key);
            if (targetTypeRef.getType() == String.class) return (R) str;
            else return JSONUtil.toBean(str, targetTypeRef.getType(), true);
        } else if(CACHE_NULL.equals(str)) {
            log.warn("{} 缓存穿透已生效, 数据层访问已拦截", key);
            return null;
        } else {
            // 缓存不存在，到数据库中查找数据
            R ret = (R) dbFallBack.apply(args);
            if(ret == null) {
                // 数据库数据不存在，缓存穿透空值处理
                log.error("{} 数据库数据不存在，空值缓存", key);
                template.opsForValue().set(key, CACHE_NULL, CACHE_NULL_TTL_MINUTES, TimeUnit.MINUTES);
            } else {
                // 数据存在，序列化缓存到redis中, 并将结果返回
                log.info("{} 数据库已查到, 刷新缓存", key);
                set(template, key, ret, timeout, unit);
                return ret;
            }
        }
        return null;
    }

    // redisson锁控制
    public static <R> R taskLock(RedissonClient red, Function<Object[], R> fallBack, String key, Object...args) {
        RLock lock = red.getLock(key);
        boolean isLocked = false;
        R ret = null;
        try {
            isLocked = lock.tryLock(REDISSON_LOCK_WAIT_MAX_SECONDS, TimeUnit.SECONDS);
            if(isLocked) {
                log.info("redisson lock获取成功，开始任务{}...", fallBack.getClass());
                ret = fallBack.apply(args);
            }
        } catch (Exception e) {
            throw new RuntimeException("锁获取失败: " + e);
        } finally {
            if(isLocked) lock.unlock();
            log.info("redisson RLock已释放");
        }
        return ret;
    }

    // 查询某个键是否存在
    public static keyStatus keyAlive(StringRedisTemplate template, String key) {
        String str = template.opsForValue().get(key);
        if(str == null) return keyStatus.NIL;
        else if(CACHE_NULL.equals(str)) return keyStatus.EMPTY;
        else return keyStatus.EXISTS;
    }

    // 删除key
    public static void removeKey(StringRedisTemplate template, String key) {
        template.delete(key);
    }

    // 刷新ttl
    public static void refreshTTL(StringRedisTemplate template, String key, Long timeout, TimeUnit unit) { template.expire(key, timeout, unit); }
}
