package com.vs.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vs.article.entity.Article;
import com.vs.article.model.dto.ArticleCardDTO;
import com.vs.article.model.dto.ArticleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    // 查询所有文章列表
    List<ArticleCardDTO> listArticleCards(@Param("current") Long current, @Param("size") Long size);

    // 获取置顶和推荐文章列表
    List<ArticleCardDTO> listTopFeaturedArticleCards();

    // 分类获取文章
    List<ArticleCardDTO> listCategoryArticleCards(@Param("current") Long current, @Param("size") Long size, @Param("categoryId") Integer categoryId);

    // 根据id获取文章
    ArticleDTO getArticle(@Param("articleId") Integer articleId);

    // 获取前后篇文章卡片
    ArticleCardDTO getPreArticleCard(@Param("articleId") Integer articleId);

    ArticleCardDTO getNextArticleCard(@Param("articleId")Integer articleId);

    // 获取最后一篇文章卡片
    ArticleCardDTO getLastArticleCard();

    // 获取第一篇文章卡片
    ArticleCardDTO getFirstArticleCard();

    // 根据标签id获取文章列表
    List<ArticleCardDTO> listTagArticles(@Param("current") Long current, @Param("size") Long size, @Param("tagId") Integer tagId);

    // 获取所有文章归档
    List<ArticleCardDTO> listArchives(@Param("current") Long pageOffset, @Param("size") Long pageSize);
}
