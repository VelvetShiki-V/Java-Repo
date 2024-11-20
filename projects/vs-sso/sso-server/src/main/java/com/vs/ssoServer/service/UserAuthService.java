package com.vs.ssoServer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vs.ssoServer.entity.UserAuth;

public interface UserAuthService extends IService<UserAuth> {

    // 邮箱登录
    void login(String name, String pwd);

}
