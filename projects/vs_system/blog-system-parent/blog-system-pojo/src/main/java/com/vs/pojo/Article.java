package com.vs.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author velvetshiki
 * @since 2024-07-22
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("article")
public class Article implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    @TableId(value = "aid", type = IdType.AUTO)
    private Integer aid;

    /**
     * 作者id
     */
    private Integer uid;

    /**
     * 作者
     */
    private String username;

    /**
     * 标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 标题图片
     */
    private String profile;

    /**
     * 文章链接
     */
    private String content;

    /**
     * 文章创建时间
     */
    private LocalDateTime createTime;

    /**
     * 文章更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 点赞
     */
    private Integer love;

    /**
     * 收藏
     */
    private Integer star;

//    文章发布状态
    private ArticleStatus status;

}
