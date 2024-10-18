package com.vs._2024_10_18.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {
    private String code = "200";
    private String msg = "empty msg";
    private Object data = "empty data";

    public static final String SUCCESS_CODE = "200";
    public static final String ERROR_CODE = "400";

//    public static Result success() {
//        return new Result("1", "操作成功", null);
//    }
//
//    public static Result success(String msg, Object data) {
//        return new Result("1", msg, data);
//    }
//
//    public static Result error(String msg) {
//        return new Result("0", msg, null);
//    }
//
//    public static Result error(String code, String msg) { return new Result(code, msg, null); }
}
