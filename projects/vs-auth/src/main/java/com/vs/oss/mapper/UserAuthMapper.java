package com.vs.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vs.auth.entity.UserAuth;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAuthMapper extends BaseMapper<UserAuth> {
}
