package com.vs.article.model.dto;

import lombok.*;
import java.util.List;

// 后台管理文章详情视图
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleAdminViewDTO {

    private Integer id;

    private String articleTitle;

    private String articleAbstract;

    private String articleContent;

    private String articleCover;

    private String categoryName;

    private List<String> tagNames;      // 借助tagService查询

    private Integer isTop;

    private Integer isFeatured;

    private Integer status;

    private Integer type;

    private String originalUrl;

    private String password;

}
