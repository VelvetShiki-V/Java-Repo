package com.vs.myemc_gms_main.exception;

import com.vs.common.CustomException;
import com.vs.pojo.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // token失效
    @ExceptionHandler(CustomException.InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result invalidTokenHandler(CustomException.InvalidTokenException e) {
        e.printStackTrace();
        return Result.error(401, e.getMessage());
    }

    // 访问被拒
    @ExceptionHandler(CustomException.AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result AccessDeniedHandler(CustomException.AccessDeniedException e) {
        e.printStackTrace();
        return Result.error(403, e.getMessage());
    }

    // 数据不存在
    @ExceptionHandler(CustomException.DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result DataNotFoundHandler(CustomException.DataNotFoundException e) {
        e.printStackTrace();
        return Result.error(404, e.getMessage());
    }

    // 内部错误
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result globalHandler(Exception e) {
        e.printStackTrace();
        return Result.error(500,"500 Internal Server Error: " + e.getMessage());
    }
}
