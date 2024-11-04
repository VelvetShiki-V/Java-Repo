package com.vs.article.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "刪除文章VO",description = "管理文章刪除", type = "ArticleDeleteVO")
public class ArticleDeleteVO {

    @NotNull(message = "删除的文章id不能为空")
    @Schema(description = "待刪除文章id", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Integer> ids;

    @NotNull(message = "删除状态值不能为空")
    @Schema(description = "判断文章是否逻辑删除", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isDelete;
}
