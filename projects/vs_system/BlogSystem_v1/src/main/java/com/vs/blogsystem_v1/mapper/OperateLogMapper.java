package com.vs.blogsystem_v1.mapper;

import com.vs.pojo.OperateLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Mapper
public interface OperateLogMapper {
    @Insert("insert into operate_log (uid, username, operate_time, class_name, method_name, method_params, return_value, time_spent) " +
            "values (#{uid}, #{username}, #{operateTime}, #{className}, #{methodName}, #{methodParams}, #{returnValue}, #{timeSpent})")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    void logRecord(OperateLog row);
}
