package com.vs.article.strategy;

import com.vs.article.model.dto.ArticleSearchDTO;
import java.util.List;

public interface ArticleSearchStrategy {

    // 找出符合索引关键词的文章列表返回并高光显示
    List<ArticleSearchDTO> searchArticles(String keywords);

}
