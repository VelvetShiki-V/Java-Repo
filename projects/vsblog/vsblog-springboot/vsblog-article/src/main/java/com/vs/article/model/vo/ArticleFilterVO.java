package com.vs.article.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "后台文章过滤VO", description = "后台文章筛选条件，接收过滤实体ArticleFilterVO", type = "ArticleFilterVO")
public class ArticleFilterVO {

    @Schema(description = "页码")
    private Long pageIndex;

    @Schema(description = "条数")
    private Long pageSize;

    @Schema(description = "文章关键字")
    private String title;

    @Schema(description = "分类id")
    private Integer categoryId;

    @Schema(description = "标签id")
    private Integer tagId;

    @Schema(description = "登录类型")
    private Integer loginType;

    @Schema(description = "文章类型")
    private Integer type;

    @Schema(description = "文章状态")
    private Integer status;

    @Schema(description = "文章发布时间")
    private LocalDateTime publishTime;

    @Schema(description = "截止时间")
    private LocalDateTime endTime;

    @Schema(description = "是否删除")
    private Integer isDeleted;

    @Schema(description = "是否审核")
    private Integer isCensored;

    @Schema(description = "是否置顶")
    private Integer isTop;

    @Schema(description = "是否推荐")
    private Integer isFeatured;
}
