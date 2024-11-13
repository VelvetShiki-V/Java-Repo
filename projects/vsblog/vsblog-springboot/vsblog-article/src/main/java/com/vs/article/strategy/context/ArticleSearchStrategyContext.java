package com.vs.article.strategy.context;

import com.vs.article.enums.ArticleSearchStrategyEnum;
import com.vs.article.model.dto.ArticleSearchDTO;
import com.vs.article.strategy.ArticleSearchStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ArticleSearchStrategyContext {

    @Value("${search.mode}")
    private String mode;

    private final Map<String, ArticleSearchStrategy> searchStrategyMap;

    public List<ArticleSearchDTO> executeSearchStrategy(String keywords) {
        return searchStrategyMap
                .get(ArticleSearchStrategyEnum.searchStrategyAdaptor(mode))
                .searchArticles(keywords);
    }
}
