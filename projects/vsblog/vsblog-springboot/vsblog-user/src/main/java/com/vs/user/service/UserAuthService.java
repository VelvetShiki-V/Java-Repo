package com.vs.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vs.user.entity.UserAuth;
import com.vs.user.model.dto.UserInfoDTO;
import com.vs.user.model.vo.UserAuthVO;

public interface UserAuthService extends IService<UserAuth> {

    // 邮箱登录
    UserInfoDTO loginEmail(UserAuthVO auth);
}
