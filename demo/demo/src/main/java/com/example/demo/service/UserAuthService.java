package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.UserAuth;

public interface UserAuthService extends IService<UserAuth> {

    // 邮箱登录
    void login(String name, String pwd);

}
