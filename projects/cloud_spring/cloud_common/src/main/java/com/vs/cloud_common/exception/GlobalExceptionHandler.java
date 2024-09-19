package com.vs.cloud_common.exception;

import com.vs.cloud_common.domain.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 自定义异常处理
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Result> CustomHandler(CustomException e) {
        e.printStackTrace();
        return new ResponseEntity<>(Result.error(e.getMessage()), e.getStatus());
    }

    // 内部错误
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> globalHandler(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(Result.error("500 Server Error: " +
                e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
