package com.example.demo.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.UserAuth;
import com.example.demo.entity.UserInfo;
import com.example.demo.exception.CustomException;
import com.example.demo.mapper.UserAuthMapper;
import com.example.demo.service.UserAuthService;
import com.example.demo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements UserAuthService {


    @Autowired
    private UserInfoService userInfoService;

    // 邮箱登录
    @Override
    public void login(String name, String pwd) {
        UserInfo userInfo = userInfoService.lambdaQuery().eq(UserInfo::getEmail, name).one();
        if(Objects.isNull(userInfo)) throw new CustomException("404", "用户不存在", HttpStatus.NOT_FOUND);
        UserAuth userAuth = lambdaQuery().eq(UserAuth::getUserInfoId, userInfo.getId()).one();
        if(Objects.isNull(userAuth)) throw new CustomException("404", "用户不存在", HttpStatus.NOT_FOUND);
        if(!userAuth.getPassword().equals(pwd))
            throw new CustomException("404", "密码错误", HttpStatus.UNAUTHORIZED);
        // 记录登录状态(7天有效)
        StpUtil.login(userAuth.getId(), new SaLoginModel().setTimeout(60 * 60 * 24 * 7));
//        log.info("登录状态: {}", StpUtil.isLogin());
    }
}
