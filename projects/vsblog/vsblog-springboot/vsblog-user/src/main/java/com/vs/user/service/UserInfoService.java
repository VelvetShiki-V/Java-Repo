package com.vs.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vs.user.entity.UserInfo;
import com.vs.user.model.dto.UserInfoDTO;

public interface UserInfoService extends IService<UserInfo> {
    UserInfoDTO fetchLoginUserInfo();
}
