package com.vs.cloud_user.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

// 自定义异常类，需要传入http状态码和错误信息
public class CustomException extends RuntimeException{
    private HttpStatus status;

    public CustomException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() { return status; }
}
