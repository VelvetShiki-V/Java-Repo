package com.vs.cloud_common.utils;

import com.vs.cloud_common.domain.UserInfo;

public class UserThreadLocalUtil {
    private static final ThreadLocal<UserInfo> threadLocalStorage = new ThreadLocal<>();

    public static void setUserInfo(UserInfo info) { threadLocalStorage.set(info); }

    public static UserInfo getUserInfo() { return threadLocalStorage.get(); }

    public static void removeUserInfo() { threadLocalStorage.remove(); }
}
