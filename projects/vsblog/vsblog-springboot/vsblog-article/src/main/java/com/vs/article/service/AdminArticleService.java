package com.vs.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vs.article.entity.Article;
import com.vs.article.model.dto.ArticleAdminDTO;
import com.vs.article.model.dto.ArticleAdminViewDTO;
import com.vs.article.model.vo.ArticleFilterVO;
import com.vs.article.model.vo.ArticleVO;
import com.vs.framework.model.dto.PageResultDTO;

public interface AdminArticleService extends IService<Article> {

    // 返回后台过滤文章
    PageResultDTO<ArticleAdminDTO> listAdminArticles(ArticleFilterVO articleFilterVO);

    // 根据id获取后台文章
    ArticleAdminViewDTO getAdminArticle(Integer articleId);

    // 修改编辑文章
    void editArticle(ArticleVO articleVO);
}
