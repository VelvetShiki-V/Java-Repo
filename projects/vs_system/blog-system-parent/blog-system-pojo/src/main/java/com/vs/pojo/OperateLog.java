package com.vs.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// util工具类依赖pojo
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperateLog {
    // log类包含: 操作人uid，名字，操作类名，方法名，方法参数和返回值，操作时间，执行方法耗时
    private Integer uid;
    private String username;
    private String className;
    private String methodName;
    private String methodParams;
    private String returnValue;
    private LocalDateTime operateTime;
    private Long timeSpent;
}
