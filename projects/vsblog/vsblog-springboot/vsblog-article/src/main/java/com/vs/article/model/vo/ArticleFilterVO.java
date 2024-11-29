package com.vs.article.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "后台文章过滤VO", description = "后台文章筛选条件，接收过滤实体ArticleFilterVO", type = "ArticleFilterVO")
public class ArticleFilterVO {

    @NotNull(message = "不能为空")
    @Schema(description = "页码")
    private Long current;

    @NotNull(message = "不能为空")
    @Schema(description = "条数")
    private Long size;

    @Schema(description = "文章关键字")
    private String keywords;

    @Schema(description = "分类id")
    private Integer categoryId;

    @Schema(description = "标签id")
    private Integer tagId;

    @Schema(description = "相册id")
    private Integer albumId;

    @Schema(description = "登录类型")
    private Integer loginType;

    @Schema(description = "文章类型")
    private Integer type;

    @Schema(description = "文章状态")
    private Integer status;

    @Schema(description = "文章发布时间")
    private LocalDateTime startTime;

    @Schema(description = "截止时间")
    private LocalDateTime endTime;

    @NotNull(message = "不能为空")
    @Schema(description = "是否删除")
    private Integer isDelete;

    @Schema(description = "是否审核")
    private Integer isReview;

    @Schema(description = "是否置顶")
    private Integer isTop;

    @Schema(description = "是否推荐")
    private Integer isFeatured;
}
