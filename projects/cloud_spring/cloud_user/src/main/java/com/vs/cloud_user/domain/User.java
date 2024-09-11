package com.vs.cloud_user.domain;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @TableId(value = "uid")
    private String uid;
    private String name;
    private String role;
    private String password;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
