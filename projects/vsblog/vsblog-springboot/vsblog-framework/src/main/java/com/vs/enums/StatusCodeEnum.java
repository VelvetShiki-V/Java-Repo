package com.vs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCodeEnum {
    SUCCESS(200, "操作成功"),

    NO_LOGIN(401, "用户未登录"),

    AUTHORIZED(403, "没有操作权限"),

    SYSTEM_ERROR(500, "系统异常"),

    FAIL(510, "操作失败"),

    VALID_ERROR(520, "参数格式不正确"),

    USERNAME_EXIST(521, "用户名已存在"),

    USERNAME_NOT_EXIST(522, "用户名不存在"),

    ARTICLE_ACCESS_FAIL(523, "文章密码认证未通过"),

    QQ_LOGIN_ERROR(531, "qq登录错误");

    private final Integer code;

    private final String desc;
}
