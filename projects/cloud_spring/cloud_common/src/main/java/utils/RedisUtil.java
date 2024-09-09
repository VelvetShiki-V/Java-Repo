package utils;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.vs.common.GlobalConstants.*;

@Slf4j
public class RedisUtil {
    // static instance
    private static StringRedisTemplate stringRedisTemplate;
    private static RedissonClient redissonClient;

//     初始化template
    public static void setStringRedisTemplate(StringRedisTemplate template) {
        stringRedisTemplate = template;
    }
    public static void setRedissonClient(RedissonClient client) {
        redissonClient = client;
    }

    // 内部类存储逻辑对象
    @Data
    public static class RedisData {
        private Object object;
        private LocalDateTime expireTime;
    }

    // 数据存储(字符串原样存储，对象JSON序列化处理)
    public static <T> void set(String key, T value, Long timeout, TimeUnit unit) {
        if(value instanceof String) stringRedisTemplate.opsForValue().set(key, (String) value, timeout, unit);
        else stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), timeout, unit);
    }

    // 数据获取
    public static <R> Object query(String key, TypeReference<R> typeRef) {
        String str = stringRedisTemplate.opsForValue().get(key);
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
    public static <R> R queryTTLWithDB(String key, TypeReference<R> targetTypeRef, Long timeout,
                                       TimeUnit unit, Function<Object[], Object> dbFallBack,
                                       Object...args) {
        // 查询数据(json串)是否存在于缓存中
        String str = stringRedisTemplate.opsForValue().get(key);
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
                stringRedisTemplate.opsForValue().set(key, CACHE_NULL, CACHE_NULL_TTL_MINUTES, TimeUnit.MINUTES);
            } else {
                // 数据存在，序列化缓存到redis中, 并将结果返回
                log.info("{} 数据库已查到, 刷新缓存", key);
                set(key, ret, timeout, unit);
                return ret;
            }
        }
        return null;
    }

    // 热点数据存储(逻辑过期 + 对象装饰器序列化 -> 缓存击穿)
    public static void setWithLogicExpire(String key, Object value, Long timeout, TimeUnit unit) {
        // 对象装饰器 + 设置逻辑过期时间
        RedisData redisData = new RedisData();
        redisData.setObject(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(timeout)));
        set(key, redisData, timeout, unit);
    }

    // 热点数据获取(数据永不过期 + 逻辑过期预防缓存击穿 + 加锁多线程异步数据获取)
    public static <R> R queryWithLogicExpire(String key, TypeReference<R> targetTypeRef, Long timeout,
                                             TimeUnit unit, Function<Object[], Object> dbFallBack,
                                             Object...args) throws ExecutionException, InterruptedException {
        // 缓存查询并判断
        String jsonStr = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isBlank(jsonStr)) {
            log.warn("{} 热点缓存不存在", key);
            return null;
        } else {
            // 命中数据
            log.info("{} 热点缓存存在", key);
            RedisData redisData = JSONUtil.toBean(jsonStr, RedisData.class);
            R obj = null;
            if (targetTypeRef.getType() == String.class) obj = (R) redisData.getObject();
            else obj = JSONUtil.toBean((JSONObject) redisData.getObject(), targetTypeRef.getType(), true);
            LocalDateTime recordTime = redisData.getExpireTime();
            // 判断数据是否逻辑过期
            if (LocalDateTime.now().isAfter(recordTime)) {
                log.warn("{} 热点数据已过期, ", key);
                // 数据已过期, 互斥锁 + 开启线程到数据库中获取最新数据
                Future<R> future;
                try {
                    // 异步线程逻辑片
                    future = threadPools.submit(() -> (R) taskLock(dbFallBack, key, args));
                } catch (Exception e) {
                    throw new RuntimeException("异步任务执行失败: " + e);
                }
                // future会在异步任务完成前阻塞
                if (future.get() != null) {
                    // 热更新到缓存，并刷新逻辑过期时间
                    log.info("{} 数据库热点数据已查到，刷新缓存", key);
                    setWithLogicExpire(key, future.get(), timeout, unit);
                } else {
                    // 数据不存在，删除缓存
                    log.info("{} 数据库热点数据不存在，缓存将移除", key);
                    removeKey(key);
                }
                return future.get();
            } else {
                log.info("{} 热点数据未过期", key);
                return obj;
            }
        }
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
    public static keyStatus keyAlive(String key) {
        String str = stringRedisTemplate.opsForValue().get(key);
        if(str == null) return keyStatus.NIL;
        else if(CACHE_NULL.equals(str)) return keyStatus.EMPTY;
        else return keyStatus.EXISTS;
    }

    // 删除key
    public static void removeKey(String key) {
        stringRedisTemplate.delete(key);
    }

    // 刷新ttl
    public static void refreshTTL(String key, Long timeout, TimeUnit unit) { stringRedisTemplate.expire(key, timeout, unit); }

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
