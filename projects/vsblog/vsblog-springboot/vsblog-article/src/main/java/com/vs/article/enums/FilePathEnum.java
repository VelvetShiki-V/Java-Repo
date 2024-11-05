package com.vs.article.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

// minIO桶内的文件路径
@Getter
@AllArgsConstructor
public enum FilePathEnum {

    AVATAR("avatar/", "头像路径"),

    ARTICLE("article/", "文章路径"),

    ARTICLE_IMAGE("image/", "文章图片路径"),

    DIR_SUFFIX("/", "后缀");

    private final String path;

    private final String desc;
}
