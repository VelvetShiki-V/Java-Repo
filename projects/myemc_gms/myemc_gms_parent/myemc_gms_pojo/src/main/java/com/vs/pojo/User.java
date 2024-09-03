package com.vs.pojo;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author velvetshiki
 * @since 2024-07-30
 */
@Data
@TableName("user")
public class User {

    // @Serial
    // private static final long serialVersionUID = 1L;

    /**
     * 用户uid
     */
    // @TableId(value = "uid", type = IdType.AUTO)
    // private Integer uid;

    /**
     * 用户名
     */
    // @TableId(value = "name")
    private String name;
    private String password;
    private String role;
}
