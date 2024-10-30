package com.vs.article.model.vo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "文章标签VO", description = "修改标签或创建新标签", type = "TagVO")
public class TagVO {

    @Schema(description = "标签Id")
    private Integer id;

    @Schema(description = "标签名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "标签名不能为空")
    private String tagName;
}
