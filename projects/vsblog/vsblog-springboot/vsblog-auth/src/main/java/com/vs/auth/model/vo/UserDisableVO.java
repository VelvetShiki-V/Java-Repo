package com.vs.auth.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "用户禁用状态", description = "包含用户id和禁用状态", type = "UserDisableVO")
public class UserDisableVO {

    @NotNull(message = "用户id不能为空")
    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @NotNull(message = "用户禁用状态不能为空")
    @Schema(description = "禁用状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isDisable;
}
