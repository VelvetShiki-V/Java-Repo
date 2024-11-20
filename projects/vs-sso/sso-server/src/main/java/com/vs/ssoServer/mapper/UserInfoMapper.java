package com.vs.ssoServer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vs.ssoServer.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
