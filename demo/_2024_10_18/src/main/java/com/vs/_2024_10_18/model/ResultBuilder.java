package com.vs._2024_10_18.model;

public class ResultBuilder {
    private final Result result;

    private ResultBuilder() { result = new Result(); }

    public static ResultBuilder aResult() { return new ResultBuilder(); }

    // 构造响应体
    public ResultBuilder code(String code) {
        result.setCode(code);
        return this;
    }

    public ResultBuilder msg(String msg) {
        result.setMsg(msg);
        return this;
    }

    public <T> ResultBuilder data(T data) {
        result.setData(data);
        return this;
    }

    public Result build() { return result; }

    // 国际化翻译
//    public ResultBuilder msg(String msg) {}
}
