package com.vs.article.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "权限文章访问VO", description = "权限文章校验，接收文章权限校验实体articlePasswordVO", type = "ArticlePasswordVO")
public class ArticlePasswordVO {

    @NotNull(message = "文章Id不能为空")
    @Schema(description = "文章Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer articleId;

    @NotBlank(message = "文章密码不能为空")
    @Schema(description = "文章访问密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "qwer1234")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "账号格式为数字以及字母")
    private String articlePassword;
}
