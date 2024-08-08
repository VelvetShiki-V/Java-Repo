package com.vs.pojo;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor(staticName = "of")
// autoResultMap开启对象与json自动转换
@TableName(value = "photo")
//@TableName(value = "photo", autoResultMap = true)
public class Photo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value="pid", type=IdType.AUTO)
    private Integer pid;
    private Integer uid;
    private String username;
//    @TableField(value = "photo_url", typeHandler = JacksonTypeHandler.class)
    // JSON串类型
    private String photoUrl;
    private LocalDateTime uploadTime;
}
