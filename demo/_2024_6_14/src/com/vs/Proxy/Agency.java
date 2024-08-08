package com.vs.Proxy;

import java.lang.reflect.*;

public class Agency {
    public static Animal createProxy(Athlete ath) {
        Animal agent = (Animal) Proxy.newProxyInstance(
            // 1. 指定类加载器
            Agency.class.getClassLoader(),
            // 2. 指定代理接口(提供给代理可以执行的接口方法)
            new Class[]{Animal.class},
            // 3. 添加给代理的接口额外内容
            new InvocationHandler() {
                // 当代理调用类方法时，额外执行其他逻辑
                @Override
                // 参数解释：代理的对象，代理的方法，调用方式传递的参数
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if("marathon".equals(method.getName())) System.out.println("ath is ready to long run through agency");
                    else if("running".equals(method.getName())) System.out.println("ath is ready jogging through agency");
                    else if("jumping".equals(method.getName())) System.out.println("ath is ready popup through agency");
                    // 代理调用对象方法传递参数
                    return method.invoke(ath, args);
                }
            }
        );
        return agent;
    }
}
