package com.vs.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vs.article.entity.Article;
import com.vs.article.model.dto.ArticleCardDTO;
import com.vs.article.model.dto.ArticleDTO;
import com.vs.article.model.dto.ArticleTopFeaturedDTO;
import com.vs.article.model.vo.ArticleVO;
import com.vs.framework.model.dto.PageResultDTO;

public interface ArticleService extends IService<Article> {

    // 获取所有文章
    PageResultDTO<ArticleCardDTO> listArticles();

    // 获取置顶推荐文章
    ArticleTopFeaturedDTO listTopFeaturedArticles();

    // 分类获取文章
    PageResultDTO<ArticleCardDTO> listCategoryArticles(Integer categoryId);

    // 根据id获取文章
    ArticleDTO getArticle(Integer articleId);
}
