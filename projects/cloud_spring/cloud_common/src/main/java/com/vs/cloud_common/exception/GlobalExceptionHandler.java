package com.vs.cloud_common.exception;

import com.vs.cloud_common.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 自定义异常处理
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Result> CustomHandler(CustomException e) {
        log.error("全局异常捕获: 自定义异常: {}", e.getMessage());
        return new ResponseEntity<>(Result.error(e.getMessage()), e.getStatus());
    }

    // 内部错误
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> globalHandler(Exception e) {
        e.printStackTrace();
        log.error("全局异常捕获: 服务器内部异常: {}", e.getMessage());
        return new ResponseEntity<>(Result.error("500 Server Error: " +
                e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
