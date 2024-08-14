package com.vs.common;

import lombok.Data;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
public class GlobalConstants {
    // 项目常量
    public static final String SYSTEM_REDIS_KEY_PREFIX = "myemc_gms:";

    // 配置文件动态常量
    public static final int REDISSON_LOCK_WAIT_MAX_SECONDS = 5;
    public static final int THREAD_POOL_SIZE_MAX = 10;
    public static final Long REDIS_CACHE_MAX_TTL_MINUTES = 30L;
    public static final Long JWT_EXPIRE_DURATION_MINUTES = 30L;     // 30min exp

//    public static int THREAD_POOL_SIZE_MAX;
//    public static Long REDISSON_LOCK_WAIT_MAX_SECONDS;
//    public static Long REDIS_CACHE_MAX_TTL_MINUTES;
//    public static Long JWT_EXPIRE_DURATION_MINUTES;    // 30min exp

//    @Autowired
//    GlobalConstants(GlobalConfig globalConfig) {
//        REDIS_CACHE_MAX_TTL_MINUTES = globalConfig.getRedis_cache_max_ttl_minutes();
//        REDISSON_LOCK_WAIT_MAX_SECONDS = globalConfig.getRedisson_lock_wait_max_seconds();
//        JWT_EXPIRE_DURATION_MINUTES = globalConfig.getJwt_expire_duration_seconds();
//        THREAD_POOL_SIZE_MAX = globalConfig.getThread_pool_max_size();
//    }

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
