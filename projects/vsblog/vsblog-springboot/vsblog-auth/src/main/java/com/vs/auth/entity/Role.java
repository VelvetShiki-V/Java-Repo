package com.vs.auth.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_role")
public class Role {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String roleName;

    private Integer isDisable;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
