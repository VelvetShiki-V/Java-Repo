package com.vs.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vs.auth.entity.UserAuth;
import com.vs.auth.model.dto.UserInfoDTO;
import com.vs.auth.model.vo.UserAuthVO;

public interface UserAuthService extends IService<UserAuth> {

    // 邮箱登录
    UserInfoDTO loginEmail(UserAuthVO auth);

    boolean checkIsAdmin(Integer loginId);
}
