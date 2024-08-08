package com.vs.blogsystem_v1.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 标识原注解来自定义注解，供特定方法AOP运行时作为切入点代理使用
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TimeRecord {
}
