package com.vs.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vs.article.entity.Article;
import com.vs.article.model.dto.ArticleCardDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    // 查询所有文章列表
    List<ArticleCardDTO> listArticles(@Param("current") Long current, @Param("size") Long size);

    // 获取置顶和推荐文章列表
    List<ArticleCardDTO> listTopFeaturedArticles();
}
