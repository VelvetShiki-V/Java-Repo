package com.vs.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vs.article.entity.Article;
import com.vs.article.model.dto.ArticleAdminDTO;
import com.vs.article.model.dto.ArticleAdminViewDTO;
import com.vs.article.model.vo.ArticleDeleteVO;
import com.vs.article.model.vo.ArticleFilterVO;
import com.vs.article.model.vo.ArticleTopFeaturedVO;
import com.vs.article.model.vo.ArticleVO;
import com.vs.framework.model.dto.PageResultDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminArticleService extends IService<Article> {

    // 返回后台过滤文章
    PageResultDTO<ArticleAdminDTO> listAdminArticles(ArticleFilterVO articleFilterVO);

    // 根据id获取后台文章
    ArticleAdminViewDTO getAdminArticle(Integer articleId);

    // 修改编辑文章
    void saveOrUpdateArticle(ArticleVO articleVO);

    // 批量更改文章删除状态
    void deleteStateChange(ArticleDeleteVO articleDeleteVO);

    // 物理删除文章
    void deleteArticle(List<Integer> articleIds);

    // 修改置顶或推荐文章状态
    void updateTopFeaturedArticles(ArticleTopFeaturedVO articleTopFeaturedVO);

    // 导入文章
    void importArticle(MultipartFile file);

    // 导出文章
    List<String> exportArticles(List<Integer> articleIds);
}
