package com.vs.cloud_common.utils;

public class UserThreadLocalUtil {
    private static final ThreadLocal<Long> threadLocalStorage = new ThreadLocal<>();

    public static void setUserUid(Long uid) { threadLocalStorage.set(uid); }

    public static String getUserUid() { return threadLocalStorage.get().toString(); }

    public static void removeUser() { threadLocalStorage.remove(); }
}
