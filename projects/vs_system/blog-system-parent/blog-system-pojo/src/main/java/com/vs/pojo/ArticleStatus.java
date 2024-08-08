package com.vs.pojo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArticleStatus {
    DRAFT(0, "草稿"), PUBLISHED(1, "已发布");
    @EnumValue
    private final Integer value;
    private final String desc;
}
