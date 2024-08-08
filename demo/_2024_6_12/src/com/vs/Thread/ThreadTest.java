package com.vs.Thread;

import java.util.concurrent.FutureTask;

public class ThreadTest {
    public void test() throws Exception {
        // 继承thread并重写run
//        MyThread t1 = new MyThread();
//        MyThread t2 = new MyThread();
//        MyThread t3 = new MyThread();
//        t1.setName("t1");
//        t2.setName("t2");
//        t3.setName("t3");
//
//        t1.start();
//        t2.start();
//        t3.start();

        // 实现Runnable接口并重写run
        MyThread task = new MyThread();
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        Thread t3 = new Thread(task);

//        t1.setName("t1");
//        t2.setName("t2");
//        t3.setName("t3");

        t1.start();
        t2.start();
        t3.start();
//        t1.join();
//        t2.join();
//        t3.join();

/*        // Callable重写call
        // 创建task对象表示多线程执行的具体任务
        MyThread th = new MyThread();
        // 创建futureTask用于管理多线程运行结果
        FutureTask<Integer> manager = new FutureTask<>(th);
        // 创建多线程用于执行任务
        Thread t1 = new Thread(manager);
        // 启动并获取结果
        t1.start();
        int ret = manager.get();
        System.out.println(ret);*/
    }
}
