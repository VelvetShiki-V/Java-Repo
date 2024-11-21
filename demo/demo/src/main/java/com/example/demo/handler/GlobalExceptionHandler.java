package com.example.demo.handler;

import cn.dev33.satoken.util.SaResult;
import com.example.demo.exception.CustomException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 自定义错误
    @ExceptionHandler(CustomException.class)
    public SaResult customExceptionHandler(CustomException e) {
        return SaResult.error(e.getMessage());
    }

    // 全局异常拦截
    @ExceptionHandler
    public SaResult handlerException(Exception e) {
        e.printStackTrace();
        return SaResult.error(e.getMessage());
    }
}
