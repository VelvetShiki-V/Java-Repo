package com.vs.Thread;

import java.util.Random;
import java.util.concurrent.*;
import java.util.random.RandomGenerator;

public class BlockingQueueTest {
    class MyBlockingQueue {
        // 设置阻塞队列容量为2，即生产者一次生产两个数据，消费者一次消费两个数据
        // 使用linked阻塞队列实例化BlockingQueue接口
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(2);
        Random rand = new Random();

        // 生产与消费数据
        public void produce() throws InterruptedException {
            int tmp = rand.nextInt(0, 100);
            queue.put(tmp);
            System.out.println("produced element: " + tmp);
        }

        public int consume() throws InterruptedException {
            return queue.take();
        }
    }

    public void test() {
        MyBlockingQueue q = new MyBlockingQueue();

        // 创建生产者与消费者线程，并运行
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; ++i) {
                try {
                    q.produce();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 10; ++i) {
                try {
                    System.out.println("消费了: " + q.consume());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        producer.start();
        consumer.start();

    }
}
