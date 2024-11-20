package com.vs.ssoServer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vs.ssoServer.entity.UserAuth;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAuthMapper extends BaseMapper<UserAuth> {
}
