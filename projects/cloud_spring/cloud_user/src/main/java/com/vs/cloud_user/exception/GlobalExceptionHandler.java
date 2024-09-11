package com.vs.cloud_user.exception;

import com.vs.cloud_user.domain.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 非法访问
    @ExceptionHandler(CustomException.BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result BadRequestException(CustomException.BadRequestException e) {
        e.printStackTrace();
        return Result.error(400, e.getMessage());
    }

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
