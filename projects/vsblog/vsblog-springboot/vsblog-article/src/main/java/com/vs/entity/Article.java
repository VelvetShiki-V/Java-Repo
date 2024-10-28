package com.vs.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * (Article)实体类
 *
 * @author makejava
 * @since 2024-10-28 17:08:08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_article")
public class Article implements Serializable {
    private static final long serialVersionUID = 958461194383817190L;

    private Integer id;
    /**
     * 作者
     */
    private Integer userId;
    /**
     * 文章分类
     */
    private Integer categoryId;
    /**
     * 文章缩略图
     */
    private String articleCover;
    /**
     * 标题
     */
    private String articleTitle;
    /**
     * 文章摘要，如果该字段为空，默认取文章的前500个字符作为摘要
     */
    private String articleAbstract;
    /**
     * 内容
     */
    private String articleContent;
    /**
     * 是否置顶 0否 1是
     */
    private Integer isTop;
    /**
     * 是否推荐 0否 1是
     */
    private Integer isFeatured;
    /**
     * 是否删除  0否 1是
     */
    private Integer isDelete;
    /**
     * 状态值 1公开 2私密 3草稿
     */
    private Integer status;
    /**
     * 文章类型 1原创 2转载 3翻译
     */
    private Integer type;
    /**
     * 访问密码
     */
    private String password;
    /**
     * 原文链接
     */
    private String originalUrl;
    /**
     * 发表时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}

