package com.vs.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vs.auth.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
