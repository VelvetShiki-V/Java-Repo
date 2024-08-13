package com.vs.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
@Component
@ConfigurationProperties(prefix = "global-dynamic-constants")
public class GlobalConstants {
    // 项目常量
    public static final String SYSTEM_REDIS_KEY_PREFIX = "myemc_gms:";

    // 动态配置常量, 注意命名应该与yml中的key对应
    public static Long REDIS_CACHE_MAX_TTL_MINUTES;
    public static Long REDISSON_LOCK_WAIT_MAX_SECONDS;
    public static Long JWT_EXPIRE_DURATION_SECONDS;     // 30min exp
    public static int THREAD_POOL_SIZE_MAX;

    // JWT相关
    public static final String JWT_PREFIX = SYSTEM_REDIS_KEY_PREFIX + "login:user:id:";

    // redis相关常量
    // 用户相关
    public static final String QUERY_USER_ALL = SYSTEM_REDIS_KEY_PREFIX + "query:user:id:all";
    public static final String QUERY_USER_PREFIX = SYSTEM_REDIS_KEY_PREFIX + "query:user:id:";
    // 数据相关
    public static final String INCREMENT_HEADER = "icr:";
    public static final String KEY_ADD_COUNT = SYSTEM_REDIS_KEY_PREFIX + "add:model:day:";
    public static final String QUERY_MODEL_ALL = SYSTEM_REDIS_KEY_PREFIX + "query:model:id:all";
    public static final String QUERY_MODEL_PREFIX = SYSTEM_REDIS_KEY_PREFIX + "query:model:Id:";
    public static final String CREATE_MODEL_PREFIX = SYSTEM_REDIS_KEY_PREFIX + "add:model:Ts:";
    public static final String REMOVE_MODEL_PREFIX = SYSTEM_REDIS_KEY_PREFIX + "rmv:model:Id:";
    // 分布式锁相关
    public static final String LOCK_HEADER = "dbLock:";
    public static final Long CACHE_NULL_TTL = 30L;
    public static final int LEFT_MOVE_BITS = 32;

    // 全局实例
    public static final ExecutorService threadPools = Executors.newFixedThreadPool(THREAD_POOL_SIZE_MAX);
}
