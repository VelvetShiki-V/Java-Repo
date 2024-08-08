package com.vs.blogsystem_v1.exception;

import com.vs.blogsystem_v1.aop.Log;
import com.vs.pojo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 全局异常捕获类
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 捕获所有异常，并统一处理(Result返回)
    @Log
    @ExceptionHandler(Exception.class)
    public Result handler(Exception e) {
        e.printStackTrace();
        return Result.error("请求错误: " + e.getMessage());
    }
}
