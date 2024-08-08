package com.vs.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer code;
    private String msg;
    private Object data;

    // CUD success
    public static Result success() {
        return new Result(1, "操作成功", null);
    }

    // CUD fail
    public static Result fail() {
        return new Result(0, "操作失败", null);
    }

    // 查询成功
    public static Result success(Object object) {
        return new Result(1, "查询成功", object);
    }

    // 查询失败
    public static Result error(String msg) {
        return new Result(0, msg, null);
    }
}
