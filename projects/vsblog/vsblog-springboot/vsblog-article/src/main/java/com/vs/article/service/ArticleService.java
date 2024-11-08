package com.vs.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vs.article.entity.Article;
import com.vs.article.model.dto.*;
import com.vs.article.model.vo.ArticleFilterVO;
import com.vs.article.model.vo.ArticlePasswordVO;
import com.vs.framework.model.dto.PageResultDTO;
import java.util.List;

public interface ArticleService extends IService<Article> {

    // 获取所有文章
    PageResultDTO<ArticleCardDTO> listArticles();

    // 获取置顶推荐文章
    ArticleTopFeaturedDTO listTopFeaturedArticles();

    // 分类获取文章
    PageResultDTO<ArticleCardDTO> listCategoryArticles(Integer categoryId);

    // 根据id获取文章
    ArticleDTO getArticle(Integer articleId);

    // 根据标签id获取文章列表
    PageResultDTO<ArticleCardDTO> listTagArticles(Integer tagId);

    // 私密文章密码校验
    void accessPrivateArticle(ArticlePasswordVO articlePasswordVO);

    // 获取所有文章归档
    PageResultDTO<ArchiveDTO> listArchives();
}
