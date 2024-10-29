package com.vs.article.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 置顶和推荐文章实体类
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopFeaturedArticlesDTO {
    private ArticleCardDTO topArticle;
    private List<ArticleCardDTO> featuredArticles;
}
