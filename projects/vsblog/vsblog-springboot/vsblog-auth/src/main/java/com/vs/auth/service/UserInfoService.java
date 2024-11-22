package com.vs.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vs.auth.entity.UserInfo;
import com.vs.auth.model.dto.UserInfoDTO;

public interface UserInfoService extends IService<UserInfo> {
    UserInfoDTO userinfo();
}
