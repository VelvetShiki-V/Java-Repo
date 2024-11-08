package com.vs.article.strategy.impl;

import com.vs.article.model.dto.ArticleSearchDTO;
import com.vs.article.strategy.ArticleSearchStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mysqlSearchStrategyImpl")
public class MysqlSearchStrategyImpl implements ArticleSearchStrategy {
    @Override
    public List<ArticleSearchDTO> searchArticles(String keywords) {
        return null;
    }
}
