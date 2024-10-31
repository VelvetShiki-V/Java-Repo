package com.vs.article.handler;

import com.vs.article.exception.CustomException;
import com.vs.framework.enums.StatusCodeEnum;
import com.vs.framework.model.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdviceHandler {
    // 自定义错误
    @ExceptionHandler(CustomException.class)
    public ResultVO<?> customExceptionHandler(CustomException e) {
        return ResultVO.fail(e.getCode(), e.getMessage());
    }

    // 内部错误
    @ExceptionHandler(Exception.class)
    public ResultVO<?> globalHandler(Exception e) {
        e.printStackTrace();
        log.error("全局异常捕获: 服务器内部异常: {}", e.getMessage());
        return ResultVO.fail(StatusCodeEnum.SYSTEM_ERROR.getCode(), StatusCodeEnum.SYSTEM_ERROR.getDesc());
    }
}
