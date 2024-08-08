package com.vs.springdemo1.pojo;

public class Result {
    // 响应码，响应信息，响应数据
    private Integer code;
    private String msg;
    private Object data;

    public Result(){}

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 静态方法快速构建Result对象
    public static Result success(String msg, Object data) {
        return new Result(200, msg, data);
    }

    public static Result success() {
        return new Result(200, "Success", null);
    }

    public static Result error(String msg) {
        return new Result(400, msg, null);
    }

    @Override
    public String toString() {
        return "{" + "'code'=" + code + ", 'msg'=" + msg + ", 'data'=" + data + "}";
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }
}
