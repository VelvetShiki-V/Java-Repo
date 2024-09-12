package com.vs.cloud_common.constants;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GlobalConstants {
    // 项目常量
    public static final String SYSTEM_REDIS_KEY_PREFIX = "cloud:";
    // 秘钥常量
    public static final String SECRET_KEY_PREFIX = SYSTEM_REDIS_KEY_PREFIX + "secretKey";
    public static final Long SECRET_KEY_EXPIRE_MILLISECONDS = 259200000L;

    // 配置文件动态常量
    public static final int THREAD_POOL_SIZE_MAX = 10;

    // redis相关常量
    public static final String CACHE_NULL = "";
    public static final Long CACHE_NULL_TTL_MINUTES = 10L;
    public static final Long REDIS_CACHE_MAX_TTL_MINUTES = 30L;
    public static final Long JWT_EXPIRE_DURATION_MINUTES = 30L;
    public static final String JWT_PREFIX = SYSTEM_REDIS_KEY_PREFIX + "login:name:";
    // 用户相关
    public static final String QUERY_USER_ALL = SYSTEM_REDIS_KEY_PREFIX + "query:user:all";
    public static final String QUERY_USER_PREFIX = SYSTEM_REDIS_KEY_PREFIX + "query:user:uid:";
    public static final String REMOVE_USER_PREFIX = SYSTEM_REDIS_KEY_PREFIX + "rm:user:uid:";
    public static final String UPDATE_USER_PREFIX = SYSTEM_REDIS_KEY_PREFIX + "upd:user:uid:";
    // 数据相关
    public static final String QUERY_MODEL_ALL = SYSTEM_REDIS_KEY_PREFIX + "query:model:all";
    public static final String QUERY_MODEL_PREFIX = SYSTEM_REDIS_KEY_PREFIX + "query:model:mid:";
    public static final String REMOVE_MODEL_PREFIX = SYSTEM_REDIS_KEY_PREFIX + "rm:model:mid:";
    public static final String UPDATE_MODEL_PREFIX = SYSTEM_REDIS_KEY_PREFIX + "upd:model:mid:";
    // 分布式锁相关
    public static final int REDISSON_LOCK_WAIT_MAX_SECONDS = 2;

    // 全局实例
    public static final ExecutorService threadPools = Executors.newFixedThreadPool(THREAD_POOL_SIZE_MAX);

    // 枚举
    public enum keyStatus {
        NIL, EMPTY, EXISTS
    }
}
