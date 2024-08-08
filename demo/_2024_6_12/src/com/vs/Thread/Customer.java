package com.vs.Thread;

public class Customer extends Thread{
    @Override
    public void run() {
        // 消费数据
        while(true) {
            synchronized(ShareData.lock) {
                // 如果生产完毕，则停止消费
                if(ShareData.total == 0) break;
                else {
                    // 消费数据为0，唤醒生产者开始生产数据
                    if(ShareData.flag == 0) {
                        try {
                            ShareData.lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        // 如果数据存在，开始消费数据
                        ShareData.total--;
                        System.out.println("消费者消费了数据，剩余: " + ShareData.total);
                        // 数据消费后，唤醒生产者开始生产数据，并让消费者停止消费数据
                        ShareData.flag = 0;
                        ShareData.lock.notifyAll();
                    }
                }
            }
        }
    }
}
