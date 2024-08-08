package com.vs.myemc_gms_main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vs.pojo.User;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author velvetshiki
 * @since 2024-07-30
 */
public interface UserService extends IService<User> {
    String loginService(User user);
}
