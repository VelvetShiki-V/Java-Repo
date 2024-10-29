package com.vs.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vs.article.entity.Article;
import com.vs.article.model.dto.ArticleCardDTO;
import com.vs.article.model.dto.TopFeaturedArticlesDTO;
import com.vs.framework.model.dto.PageResultDTO;

public interface ArticleService extends IService<Article> {

    // 获取所有文章
    PageResultDTO<ArticleCardDTO> listArticles();

    // 获取置顶推荐文章
    TopFeaturedArticlesDTO listTopFeaturedArticles();
}
