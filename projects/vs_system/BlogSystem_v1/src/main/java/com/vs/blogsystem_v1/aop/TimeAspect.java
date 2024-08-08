package com.vs.blogsystem_v1.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

import java.util.Arrays;

@Component      // 成为IOC bean对象
@Aspect         // AOP切面的动态代理类
@Slf4j
public class TimeAspect {
    // 抽取切入点表达式, public为所有外部切面类均可使用（需带上全类名.pt()）
//    @Pointcut("execution(* com.vs.blogsystem_v1.service.*.*(..))")
    @Pointcut("@annotation(com.vs.blogsystem_v1.aop.TimeRecord)")
    public void cutAllServices() {}

    // AOP动态代理执行方法
    @Before("cutAllServices()")
    public void beforeOutput(JoinPoint joinPoint)  {
        // 获取代理方法类名
        log.info("获取执行AOP执行函数类名: {}", joinPoint.getTarget().getClass().getName());
        // 获取代理方法名
        log.info("获取执行AOP执行函数名: {}", joinPoint.getSignature().getName());
        // 获取代理方法参数
        log.info("获取执行AOP执行函数参数: {}", Arrays.toString(joinPoint.getArgs()));
    }
    // output
    // 2024-06-28T14:29:55.944+08:00  INFO 34204 --- [BlogSystem_v1] [0.1-8080-exec-1] com.vs.blogsystem_v1.aop.TimeAspect      : 获取执行AOP执行函数类名: com.vs.blogsystem_v1.service.impl.UserServiceImpl
    // 2024-06-28T14:29:55.945+08:00  INFO 34204 --- [BlogSystem_v1] [0.1-8080-exec-1] com.vs.blogsystem_v1.aop.TimeAspect      : 获取执行AOP执行函数名: userRegister
    // 2024-06-28T14:29:55.947+08:00  INFO 34204 --- [BlogSystem_v1] [0.1-8080-exec-1] com.vs.blogsystem_v1.aop.TimeAspect      : 获取执行AOP执行函数参数: [User(uid=null, username=sh222, password=123123, tel=15398591231, addr=null, profile=null, createTime=null, updateTime=null)]


    // 三个*分别代表：返回值，类或接口，方法，括号中为形参过滤
//    @Around("execution(* com.vs.blogsystem_v1.service.*.*(..))")        // 切入点表达式（指定需要对哪些类，接口和方法进行AOP切面管理）
    @Around("cutAllServices()")
    public Object recordRunTime(ProceedingJoinPoint joinPoint) throws Throwable {
        // 新增时间记录
        // 开始记录时间
        long start = System.currentTimeMillis();
        // 执行原始方法（连接点）
        Object ret = joinPoint.proceed();
        // 结束时间记录
        long end = System.currentTimeMillis();
        log.info("{}方法执行耗时: {}ms", joinPoint.getSignature(), end - start);
        // 返回原始方法结果
        // 通过连接点获取AOP代理对象返回值
        log.info("获取执行AOP执行函数返回值: {}", ret);
        return ret;
    }
    // output
    // 2024-06-28T14:31:28.346+08:00  INFO 34204 --- [BlogSystem_v1] [0.1-8080-exec-5] com.vs.blogsystem_v1.aop.TimeAspect      : User com.vs.blogsystem_v1.service.impl.UserServiceImpl.userLogin(User)方法执行耗时: 31ms
    // 2024-06-28T14:31:28.348+08:00  INFO 34204 --- [BlogSystem_v1] [0.1-8080-exec-5] com.vs.blogsystem_v1.aop.TimeAspect      : 获取执行AOP执行函数返回值: User(uid=2, username=velvet, password=321cxz, tel=1532021119, addr=residence, profile=www.velvetshiki.cn, createTime=2024-06-21T10:41:47, updateTime=2024-06-21T10:41:49)
}
