package com.vs.Thread;

public class Producer implements Runnable {
    @Override
    public void run() {
        // 如果消费总数为0则停止生产
        while (true) {
            synchronized (ShareData.lock) {
                if (ShareData.total == 0) break;
                else {
                    // 如果消费者消费完毕数据，开始生产
                    if (ShareData.flag == 0) {
                        System.out.println(String.format("生产了一个数据, 总量剩余：%d, 开始唤醒消费者", ShareData.total));
                        ShareData.flag = 1;
                        ShareData.lock.notifyAll();
                    } else {
                        // 如果消费者没消费数据，进入等待
                        try {
                            ShareData.lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }
}
