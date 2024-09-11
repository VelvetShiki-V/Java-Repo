package com.vs.cloud_user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vs.cloud_user.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {}
