package com.vs.auth.service.impl;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.oauth2.SaOAuth2Manager;
import cn.dev33.satoken.oauth2.template.SaOAuth2Util;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vs.auth.entity.UserInfo;
import com.vs.auth.mapper.UserInfoMapper;
import com.vs.auth.model.dto.UserInfoDTO;
import com.vs.auth.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {


    @Override
    public UserInfoDTO userinfo() {
        // 获取 Access-Token 对应的账号id
        String accessToken = SaOAuth2Manager.getDataResolver().readAccessToken(SaHolder.getRequest());
        Object loginId = SaOAuth2Util.getLoginIdByAccessToken(accessToken);
        System.out.println("-------- 此Access-Token对应的账号id: " + loginId);

        // 校验 Access-Token 是否具有权限: userinfo
        SaOAuth2Util.checkAccessTokenScope(accessToken, "userinfo");

        // 模拟账号信息 （真实环境需要查询数据库获取信息）
        UserInfo userInfo = lambdaQuery().eq(UserInfo::getId, loginId).one();
        UserInfoDTO loginUserInfo = new UserInfoDTO();
        BeanUtil.copyProperties(userInfo, loginUserInfo, CopyOptions.create().setIgnoreNullValue(true));
        return loginUserInfo;
    }
}
