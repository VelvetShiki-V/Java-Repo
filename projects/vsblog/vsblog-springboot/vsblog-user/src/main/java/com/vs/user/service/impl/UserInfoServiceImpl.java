package com.vs.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vs.user.entity.UserInfo;
import com.vs.user.mapper.UserInfoMapper;
import com.vs.user.model.dto.UserInfoDTO;
import com.vs.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {


    @Override
    public UserInfoDTO fetchLoginUserInfo() {
        UserInfo info = lambdaQuery().eq(UserInfo::getId, StpUtil.getLoginId()).one();
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtil.copyProperties(info, userInfoDTO, CopyOptions.create().setIgnoreNullValue(true));
        return userInfoDTO;
    }
}
