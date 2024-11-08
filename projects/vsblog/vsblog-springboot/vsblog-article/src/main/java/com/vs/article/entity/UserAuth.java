package com.vs.article.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user_auth")
public class UserAuth {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userInfoId;

    private String username;

    private String password;

    private Integer loginType;

    private String ipAddress;

    private String ipSource;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    private LocalDateTime lastLoginTime;
}
