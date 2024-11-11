package com.vs.framework.model.dto;

import com.vs.framework.enums.StatusCodeEnum;
import lombok.*;
import static com.vs.framework.enums.StatusCodeEnum.*;

// 统一响应格式
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultDTO<T> {

    private Boolean flag;

    private Integer code;

    private String message;

    private T data;

    public static <T> ResultDTO<T> ok() {
        return resultVO(true, SUCCESS.getCode(), SUCCESS.getDesc(), null);
    }

    public static <T> ResultDTO<T> ok(T data) {
        return resultVO(true, SUCCESS.getCode(), SUCCESS.getDesc(), data);
    }

    public static <T> ResultDTO<T> ok(T data, String message) {
        return resultVO(true, SUCCESS.getCode(), message, data);
    }

    public static <T> ResultDTO<T> fail() {
        return resultVO(false, FAIL.getCode(), FAIL.getDesc(), null);
    }

    public static <T> ResultDTO<T> fail(StatusCodeEnum statusCodeEnum) {
        return resultVO(false, statusCodeEnum.getCode(), statusCodeEnum.getDesc(), null);
    }

    public static <T> ResultDTO<T> fail(String message) {
        return resultVO(false, message);
    }

    public static <T> ResultDTO<T> fail(T data) {
        return resultVO(false, FAIL.getCode(), FAIL.getDesc(), data);
    }

    public static <T> ResultDTO<T> fail(T data, String message) {
        return resultVO(false, FAIL.getCode(), message, data);
    }

    public static <T> ResultDTO<T> fail(Integer code, String message) {
        return resultVO(false, code, message, null);
    }

    private static <T> ResultDTO<T> resultVO(Boolean flag, String message) {
        return ResultDTO.<T>builder()
                .flag(flag)
                .code(flag ? SUCCESS.getCode() : FAIL.getCode())
                .message(message).build();
    }

    private static <T> ResultDTO<T> resultVO(Boolean flag, Integer code, String message, T data) {
        return ResultDTO.<T>builder()
                .flag(flag)
                .code(code)
                .message(message)
                .data(data).build();
    }
}
