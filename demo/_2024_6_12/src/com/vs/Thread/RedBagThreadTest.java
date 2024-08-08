package com.vs.Thread;

import java.util.*;
import java.util.Random;

public class RedBagThreadTest {
    class RedBag {
        private int bag1;
        private int bag2;
        private int bag3;
        private int flag = 3;

        RedBag() {
            bag1 = new Random().nextInt(10, 50);
            bag2 = new Random().nextInt(20, 40);
            bag3 = 100 - bag1 - bag2;
        }

        public synchronized int compete() {
            int cash = flag == 3 ? bag3 : flag == 2 ? bag2 : flag == 1 ? bag1 : 0;
            flag--;
            return cash;
        }

        @Override
        public String toString() {
            return "RedBag{" +
                    "bag1=" + bag1 +
                    ", bag2=" + bag2 +
                    ", bag3=" + bag3 +
                    '}';
        }
    }

    class pilotThread extends Thread {
        private static RedBag rg;
        private int _cash;

        {
            rg = new RedBag();
        }

        @Override
        public void run() {
            // 抢红包
            System.out.println(getName() + "正在抢");
            set_cash(rg.compete());
        }

        void set_cash(int cash) {
            this._cash = cash;
        }

        @Override
        public String toString() {
            return _cash == 0 ? getName() + "没抢到" : getName() + "抢到了" + _cash;
        }
    }

    public void test() {
        List<pilotThread> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new pilotThread());
            list.get(i).start();
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\n");

        for( pilotThread p : list) {
            System.out.println(p);
        }
    }
}
