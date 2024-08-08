package com.vs.Thread;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

//public class MyThread extends Thread {
//    // 所有线程共享数据
//    private static int tickets = 100;
//    // 锁对象一定是唯一的，这样所有线程共享同一把锁
////    static Object obj = new Object();
//
//    @Override
//    public void run() {
//        // 多线程执行逻辑
//        for (int i = 0; i < 100; i++) {
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            // 同步代码块
//            synchronized (MyThread.class) {
//                if (tickets == 0) break;
//                System.out.println(getName() + ": " + tickets);
//                tickets--;
//            }
//        }
//    }
//}

public class MyThread implements Runnable {
    int tickets = 100;
    Lock lock = new ReentrantLock();

    @Override
    public void run() {
        for(int i = 0; i < 100; i++) {
            lock.lock();
            try {
                if(tickets == 0) break;
                System.out.println(Thread.currentThread().getName() + ": " + tickets);
                tickets--;
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }
}

//public class MyThread implements Callable<Integer> {
//    @Override
//    public Integer call() throws Exception {
//        int sum = 0;
//        for(int i = 0; i <= 100; i++) {
//            sum += i;
//        }
//        return sum;
//    }
//}
