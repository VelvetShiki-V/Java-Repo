package com.vs.cloud_user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vs.cloud_common.domain.Result;
import com.vs.cloud_user.domain.User;

public interface UserService extends IService<User> {
        Result loginService(User user);

        Result userQuery(String uid);

        Result userCreate(User user);

        Result userDelete(String uid);

        Result userUpdate(User user);
}