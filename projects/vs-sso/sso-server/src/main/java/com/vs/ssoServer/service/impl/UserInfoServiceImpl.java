package com.vs.ssoServer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vs.ssoServer.entity.UserInfo;
import com.vs.ssoServer.mapper.UserInfoMapper;
import com.vs.ssoServer.service.UserInfoService;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
}
