package com.vs.user.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vs.user.entity.UserAuth;
import com.vs.user.entity.UserInfo;
import com.vs.user.exception.CustomException;
import com.vs.user.mapper.UserAuthMapper;
import com.vs.user.model.dto.UserInfoDTO;
import com.vs.user.model.vo.UserAuthVO;
import com.vs.user.service.UserAuthService;
import com.vs.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements UserAuthService {

    private final UserAuthMapper userAuthMapper;

    private final UserInfoService userInfoService;

    // 邮箱登录C:\Windows\System32\drivers\etc
    @Override
    public UserInfoDTO loginEmail(UserAuthVO auth) {
        UserInfo userInfo = userInfoService.lambdaQuery().eq(UserInfo::getEmail, auth.getUsername()).one();
        if(Objects.isNull(userInfo)) throw new CustomException("404", "用户不存在", HttpStatus.NOT_FOUND);
        UserAuth userAuth = lambdaQuery().eq(UserAuth::getUserInfoId, userInfo.getId()).one();
        if(Objects.isNull(userAuth)) throw new CustomException("404", "用户不存在", HttpStatus.NOT_FOUND);
        if(!userAuth.getPassword().equals(auth.getPassword()))
            throw new CustomException("400", "密码错误", HttpStatus.UNAUTHORIZED);
        UserInfoDTO data = new UserInfoDTO();
        BeanUtil.copyProperties(userInfo, data, CopyOptions.create().setIgnoreNullValue(true));
        // 记录登录状态(7天有效)
        StpUtil.login(userAuth.getId(), new SaLoginModel().setTimeout(60 * 60 * 24 * 7));
        log.info("登录状态: {}", StpUtil.isLogin());
        return data;
    }
}
