package com.vs.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vs.article.entity.Article;
import com.vs.article.model.dto.ArticleAdminDTO;
import com.vs.article.model.vo.ArticleFilterVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AdminArticleMapper extends BaseMapper<Article> {
    // 后台筛选文章数量查询
    Integer countAdminArticles(@Param("articleFilterVO") ArticleFilterVO articleFilterVO);

    // 后台筛选文章列表查询
    List<ArticleAdminDTO> listAdminArticles(@Param("current") Long current,
                                            @Param("size") Long size,
                                            @Param("articleFilterVO") ArticleFilterVO articleFilterVO);
}
