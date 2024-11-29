package com.vs.article.handler;

import com.vs.article.exception.CustomException;
import com.vs.framework.enums.StatusCodeEnum;
import com.vs.framework.model.dto.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdviceHandler {
    // 自定义错误
    @ExceptionHandler(CustomException.class)
    public ResultDTO<?> customExceptionHandler(CustomException e) {
        return ResultDTO.fail(e.getCode(), e.getMessage());
    }

    // 内部错误
    @ExceptionHandler(Exception.class)
    public ResultDTO<?> globalHandler(Exception e) {
        e.printStackTrace();
        return ResultDTO.fail(StatusCodeEnum.SYSTEM_ERROR.getCode(), StatusCodeEnum.SYSTEM_ERROR.getDesc() + ": " + e);
    }
}
