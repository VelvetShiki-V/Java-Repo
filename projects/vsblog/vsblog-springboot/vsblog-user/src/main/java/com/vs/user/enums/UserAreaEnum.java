package com.vs.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserAreaEnum {

    USER(1, "用户"),

    VISITOR(2, "游客");

    private final Integer type;

    private final String desc;

    public static UserAreaEnum getUserAreaType(Integer type) {
        for (UserAreaEnum value : UserAreaEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
