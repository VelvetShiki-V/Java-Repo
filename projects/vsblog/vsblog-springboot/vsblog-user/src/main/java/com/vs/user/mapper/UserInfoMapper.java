package com.vs.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vs.user.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
