package com.vs.auth.model.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAdminDTO {

    private Integer id;

    private Integer userInfoId;

    private String avatar;

    private String nickname;

    private List<UserRoleDTO> roles;

    private Integer loginType;

    private String ipAddress;

    private String ipSource;

    private LocalDateTime createTime;

    private LocalDateTime lastLoginTime;

    private Integer isDisable;

    private Integer status;

}
