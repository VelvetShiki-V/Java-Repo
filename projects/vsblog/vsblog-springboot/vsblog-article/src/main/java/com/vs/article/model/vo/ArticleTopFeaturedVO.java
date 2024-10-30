package com.vs.article.model.vo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "置顶推荐文章VO", description = "判断一篇文章是否是置顶或推荐", type = "ArticleTopFeaturedVO")
public class ArticleTopFeaturedVO {

    @NotNull(message = "文章id不能为空")
    @Schema(description = "文章id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @NotNull(message = "是否置顶不能为空")
    @Schema(description = "是否置顶", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isTop;

    @NotNull(message = "是否推荐不能为空")
    @Schema(description = "是否推荐", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isFeatured;
}
