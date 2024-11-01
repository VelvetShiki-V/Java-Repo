package com.vs.article.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Date createTime;
    //更新时间
    private Date updateTime;

}

