import com.vs.Thread.BlockingQueueTest;
import com.vs.Thread.PCTest;
import com.vs.Thread.ThreadTest;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import com.vs.Thread.*;
import com.vs.ThreadPool.ThreadPoolTest;

public class main {
    public static void main(String[] args) {
////        ThreadTest t = new ThreadTest();
////        try {
////            t.test();
////        } catch (Exception e) {
////            throw new RuntimeException(e);
////        }
//
//        // main线程
//*//*        for(int i = 0 ; i < 10; i++) {
//            System.out.println(Thread.currentThread().getName() + i);
//        }*//*
//
//        // 生产者消费者测试
//        PCTest t = new PCTest();
//        t.test();

        // 阻塞队列
//        BlockingQueueTest bt = new BlockingQueueTest();
//        bt.test();

        // 双线程打印奇数
        /*        class FetchOdd implements Runnable {
            Lock lock = new ReentrantLock();
            static int i = 1;

            @Override
            public void run() {
                while (i < 100) {
                    try {
                        lock.lock();
                        if(i < 100) System.out.println(Thread.currentThread().getName() + " get: " + i);
                        i += 2;
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }
        FetchOdd task = new FetchOdd();

        Thread t1 = new Thread(task, "t1");
        Thread t2 = new Thread(task, "t2");

        t1.start();
        t2.start();*/

        // 抢红包
/*        RedBagThreadTest rt = new RedBagThreadTest();
        rt.test();*/
        // 线程池
        ThreadPoolTest pool = new ThreadPoolTest();
        pool.test();
    }

    /*public static void main(String[] args) {
        class ProducerConsumer {
            private final Object lock = new Object();
            private final List<Integer> list = new ArrayList<>();
            private static final int CAPACITY = 10;

            public void produce() {
                synchronized (lock) {
                    while (list.size() == CAPACITY) {
                        try {
                            lock.wait(); // 等待，直到有空间生产
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    // 模拟生产数据
                    list.add(new Random().nextInt(100));
                    System.out.println("Produced: " + list.get(list.size() - 1));
                    lock.notifyAll(); // 唤醒所有等待的消费者线程
                }
            }

            public void consume() {
                synchronized (lock) {
                    while (list.isEmpty()) {
                        try {
                            lock.wait(); // 等待，直到有数据消费
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    // 模拟消费数据
                    int data = list.remove(0);
                    System.out.println("Consumed: " + data);
                    lock.notifyAll(); // 唤醒所有等待的生产者线程
                }
            }

        }

        ProducerConsumer pc = new ProducerConsumer();
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                pc.produce();
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                pc.consume();
            }
        });

        producer.start();
        consumer.start();
    }*/
}
