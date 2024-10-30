package com.vs.article.model.dto;

import lombok.*;

import java.util.List;

// 置顶和推荐文章实体类
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTopFeaturedDTO {
    private ArticleCardDTO topArticle;
    private List<ArticleCardDTO> featuredArticles;
}
