package com.vs.article.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArticleSearchStrategyEnum {

    MYSQL("mysql", "mysqlSearchStrategyImpl"),

    ELASTICSEARCH("elasticsearch", "esSearchStrategyImpl");

    private final String mode;

    private final String strategy;

    public static String searchStrategyAdaptor(String mode) {
        for(ArticleSearchStrategyEnum value: ArticleSearchStrategyEnum.values()) {
            if(value.getMode().equals(mode)) return value.getStrategy();
        }
        return null;
    }
}
