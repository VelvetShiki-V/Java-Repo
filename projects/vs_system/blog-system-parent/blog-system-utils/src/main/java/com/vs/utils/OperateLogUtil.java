package com.vs.utils;

import com.alibaba.fastjson.JSONObject;
import com.vs.pojo.OperateLog;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

@Slf4j
public class OperateLogUtil {
    public static OperateLog operationRecord(Map<String, Object> record) {
        // parse record
        ProceedingJoinPoint joinPoint = (ProceedingJoinPoint) record.get("joinPoint");
        String ret = record.get("returnValue") == null ? null : JSONObject.toJSONString(record.get("returnValue"));
        Long timeSpent = record.get("timeSpent") == null ? null : (Long) record.get("timeSpent");
        // 从token中获取用户uid, username
        String token = record.get("token") == null ? null : record.get("token").toString();
        Claims operator = JwtUtil.jwtParse(token);

        // setter
        // set uid, username, className, methodName, params, returnVal, operateTime, timeSpent
        OperateLog operateLog = new OperateLog();
        operateLog.setUid(operator.get("uid") == null ? null : (Integer) operator.get("uid"));     // 设置uid
        operateLog.setUsername(operator.get("username") == null ? null : operator.get("username").toString());     // 设置username
        operateLog.setMethodParams(Arrays.toString(joinPoint.getArgs()));    // 设置方法参数
        operateLog.setClassName(joinPoint.getTarget().getClass().getName());    // 设置类名
        operateLog.setMethodName(joinPoint.getSignature().getName());   // 设置方法名
        operateLog.setOperateTime(TimeFormatUtil.timeFormat(LocalDateTime.now()));  // 设置操作时间
        operateLog.setTimeSpent(timeSpent);     // 设置持续时间
        operateLog.setReturnValue(ret);     // 设置返回值, 以json存储

        // getter & print
        log.info("\n记录操作logRecord: {}\n", operateLog);

        // 将封装好的日志bean返回
        return operateLog;
    }
}

