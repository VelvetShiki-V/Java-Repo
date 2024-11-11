package com.vs.auth.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "用户权限", description = "包含用户昵称，个人介绍和网站信息", type = "UserInfoVO")
public class UserRoleVO {

    @NotBlank(message = "用户id不能为空")
    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer userInfoId;

    @NotBlank(message = "用户昵称不能为空")
    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nickname;

    @NotNull(message = "用户角色不能为空")
    @Schema(description = "用户角色列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Integer> roleIds;
}
