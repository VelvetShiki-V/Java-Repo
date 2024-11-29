package com.vs.article.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_tag")
public class Tag {

    private Integer id;

    //标签名
    private String tagName;

    //创建时间
    private LocalDateTime createTime;

    //更新时间
    private LocalDateTime updateTime;

}

