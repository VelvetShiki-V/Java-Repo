package com.vs.ssoServer.handler;

import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.vs.ssoServer.exception.CustomException;

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
