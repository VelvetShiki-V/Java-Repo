package com.vs.article.model.vo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "分类VO", description = "修改或创建文章分类", type = "CategoryVO")
public class CategoryVO {

    @Schema(description = "分类id")
    private Integer id;

    @NotBlank(message = "分类名不能为空")
    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String categoryName;
}
