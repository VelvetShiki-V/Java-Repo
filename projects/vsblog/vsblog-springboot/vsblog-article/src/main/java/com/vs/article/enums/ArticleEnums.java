package com.vs.article.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ArticleEnums {

    @Getter
    @AllArgsConstructor
    public enum Top {

        IS_NOT_TOP(0, "不置顶"),

        IS_TOP(1, "置顶");

        private final Integer value;

        private final String desc;

    }

    @Getter
    @AllArgsConstructor
    public enum Featured {

        IS_NOT_FEATURED(0, "不推荐"),

        IS_FEATURED(1, "推荐");

        private final Integer value;

        private final String desc;

    }

    @Getter
    @AllArgsConstructor
    public enum Delete {

        IS_NOT_DELETED(0, "未删除"),

        IS_DELETED(1, "删除");

        private final Integer value;

        private final String desc;

    }

    @Getter
    @AllArgsConstructor
    public enum Status {

         PUBLIC(1, "公开"),

         PRIVATE(2, "私密"),

         DRAFT(3, "草稿");

        private final Integer value;

        private final String desc;

    }

    @Getter
    @AllArgsConstructor
    public enum Type {

         ORIGINAL(1, "原创"),

         FORWARD(2, "转载"),

         TRANSLATION(3, "翻译");

        private final Integer value;

        private final String desc;

    }
}
