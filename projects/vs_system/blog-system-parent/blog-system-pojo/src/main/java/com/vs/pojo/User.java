package com.vs.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class User {
    @TableId(value="uid", type= IdType.AUTO)
    private Integer uid;
    private String username;
    private String password;
    private String role;
    private String tel;
    private String addr;
    private String profile;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
