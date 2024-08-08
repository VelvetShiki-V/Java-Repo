package com.vs.exception;

import java.util.*;

public class ExceptionTest {
    // 运行时异常，继承RuntimeException
    class No69Exception extends RuntimeException {
        // 存在两个构造，用于传递错误信息
        public No69Exception() {
        }

        public No69Exception(String message) {
            super(message);
        }
    }

    class NoPerfectException extends RuntimeException {
        public NoPerfectException() {
        }

        public NoPerfectException(String message) {
            super(message);
        }
    }

    public void test() {
        try {
            List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 69, 100));
            // 越界异常
            // list.get(10);

            // 运算异常
            // Integer ret = list.get(0) / 0;

            // 空指针异常
            // int[] arr = null;
            // int i = arr[1];

            // 自定义异常
//            if(list.contains(69)) throw new No69Exception();
            if (list.contains(100)) throw new NoPerfectException("NOOOOOOOOOOOO!");

        } catch (Exception e) {
            if (e instanceof IndexOutOfBoundsException) System.out.println("越界异常");
            else if (e instanceof ArithmeticException) System.out.println("运算异常");
            else if (e instanceof NullPointerException) System.out.println("空指针异常");
            else {
                // 观察异常类型
                System.out.println(e.getClass());
                // 若传递了异常参数，通过Message获取
                System.out.println(e.getMessage());     // output: NOOOOOOOOOOOO!
                // 完整打印异常信息(highlight)
                e.printStackTrace();
            }
            System.out.println("catch后代码执行了...");

            // 异常方法打印观察
//            System.out.println(e.getMessage());
//            System.out.println(e.toString());
//            e.printStackTrace();
//            System.out.println("数组越界");
        }
    }
}
