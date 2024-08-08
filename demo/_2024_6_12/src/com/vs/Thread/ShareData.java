package com.vs.Thread;

public class ShareData {
    // 生产总数
    public static int total = 10;
    // 是否需要生产状态控制
    public static int flag = 0;
    // 锁
    public static Object lock = new Object();
}
