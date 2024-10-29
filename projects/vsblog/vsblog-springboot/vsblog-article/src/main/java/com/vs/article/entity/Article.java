package com.vs.article.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_article")
public class Article {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer categoryId;

    private String articleCover;

    private String articleTitle;

    private String articleAbstract;     // 文章摘要，如果该字段为空，默认取文章的前500个字符作为摘要

    private String articleContent;

    private Integer isTop;              // 是否置顶 0否 1是

    private Integer isFeatured;

    private Integer isDelete;           // 是否删除  0否 1是

    private Integer status;             // 状态值 1公开 2私密 3草稿

    private Integer type;               // 文章类型 1原创 2转载 3翻译

    private String password;

    private String originalUrl;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}
