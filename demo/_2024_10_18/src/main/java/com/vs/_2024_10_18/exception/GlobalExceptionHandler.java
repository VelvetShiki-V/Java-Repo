package com.vs._2024_10_18.exception;

import com.vs._2024_10_18.model.Result;
import com.vs._2024_10_18.model.ResultBuilder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 自定义错误
    @ExceptionHandler(CustomException.class)
    public Result customExceptionHandler(HttpServletResponse response, CustomException e) {
        response.setStatus(e.getStatus().value());
        return ResultBuilder
                .aResult()
                .code(e.getCode())
                .msg(e.getMessage())
                .build();
    }

    // 内部错误
    @ExceptionHandler(Exception.class)
    public Result globalHandler(Exception e) {
        e.printStackTrace();
        log.error("全局异常捕获: 服务器内部异常: {}", e.getMessage());
        return ResultBuilder
                .aResult()
//                .code(HTT)
                .msg(e.getMessage())
                .build();
    }
}
