package com.vs.article.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "文章管理员视图VO",description = "后台管理的文章视图", type = "ArticleAdminViewVO")
public class ArticleAdminViewVO {

    @NotNull(message = "文章id不能为空")
    @Schema(description = "文章id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @Schema(description = "文章标题")
    private String articleTitle;

    @Schema(description = "文章摘要")
    private String articleAbstract;

    @Schema(description = "文章内容")
    private String articleContent;

    @Schema(description = "文章封面")
    private String articleCover;

    @Schema(description = "文章分类名")
    private String categoryName;

    @Schema(description = "标签名组")
    private List<String> tagNames;

    @Schema(description = "是否置顶")
    private Integer isTop;

    @Schema(description = "是否推荐")
    private Integer isFeatured;

    @Schema(description = "文章状态")
    private Integer status;

    @Schema(description = "文章类型")
    private Integer type;

    @Schema(description = "文章源地址")
    private String originalUrl;

    @Schema(description = "文章访问密码")
    private String password;

}
