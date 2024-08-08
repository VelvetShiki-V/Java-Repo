package com.vs.ThreadPool;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {
    public void test() {
        // 参数构建线程池, 分别为: 核心线程数，最大线程数，临时线程保活时间和单位，任务队列，线程工厂创建，任务抛弃策略
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 6,
60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(3), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        for (int i = 0 ; i < 3; i++) {
            // 分三个线程分别处理
            executor.submit(() -> {
                for(int j = 0 ; j < 10; j++) {
                    System.out.println(Thread.currentThread().getName() + " execute: " + j);
                }
            });
        }
        // 关闭线程池
        executor.shutdown();
        // 异常捕获
        try {
            if(!executor.awaitTermination(60, TimeUnit.SECONDS)) executor.shutdown();
        } catch (InterruptedException e) {
            executor.shutdownNow();
            e.printStackTrace();
        }
    }
}
