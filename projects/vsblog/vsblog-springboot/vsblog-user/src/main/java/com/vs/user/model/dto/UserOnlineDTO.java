package com.vs.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOnlineDTO {

    private Integer userInfoId;

    private String nickname;

    private String avatar;

    private String ipAddress;

    private String ipSource;

    private String browser;

    private String os;

    private LocalDateTime lastLoginTime;

}
