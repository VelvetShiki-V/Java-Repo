package com.vs.Thread;

public class PCTest {
    public void test() {
        // 创建生产者与消费者线程
        Customer guest = new Customer();
        Producer task = new Producer();
        Thread cook = new Thread(task);
        guest.setName("guest");
        cook.setName("cook");

        guest.start();
        cook.start();
    }
}
