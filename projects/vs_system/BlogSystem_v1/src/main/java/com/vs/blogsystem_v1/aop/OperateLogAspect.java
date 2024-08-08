package com.vs.blogsystem_v1.aop;

import com.vs.blogsystem_v1.mapper.OperateLogMapper;
import com.vs.pojo.OperateLog;
import com.vs.utils.OperateLogUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Aspect         // 成为AOP切面类
@Component      // 成为IOC bean对象
@Slf4j          // 记录日志
public class OperateLogAspect {
    // 相关依赖注入
    @Autowired      // 用于log数据库操作
    private OperateLogMapper operateLogMapper;
    @Autowired      // 用于从请求头获取token(进而获取用户信息，记录到日志数据库中)
    private HttpServletRequest req;

    // 通过自定义注解设置AOP代理切入点
    @Pointcut("@annotation(com.vs.blogsystem_v1.aop.Log)")
    private void logCut() {}

    // 环绕型AOP代理实例
    @Around("logCut()")
    public Object operationLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("#################AOP日志代理--开始记录操作#################");

        // 1. 记录时间
        long start = System.currentTimeMillis();
        Object ret = joinPoint.proceed();       // 2. 执行原始方法
        log.info("AOP接收到方法返回值ret: {}", ret);
        long end = System.currentTimeMillis();
        long timeSpent = end - start;

        // 3. 将操作记录封装为log bean
        Map<String, Object> record = new HashMap<>();
        record.put("token", req.getHeader("token"));
        record.put("joinPoint", joinPoint);
        record.put("returnValue", ret);
        record.put("timeSpent", timeSpent);
        OperateLog row = OperateLogUtil.operationRecord(record);

        // 4. log数据库持久化
        operateLogMapper.logRecord(row);
        log.info("#################AOP日志代理--结束记录操作#################");
        return ret;
    }
}
