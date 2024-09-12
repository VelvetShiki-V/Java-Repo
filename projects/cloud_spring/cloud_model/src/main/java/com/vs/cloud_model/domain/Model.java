package com.vs.cloud_model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Model {
    @TableId(value = "mid")
    private String mid;
    private String name;
    private String owner;
    private String labels;
    private String properties;
    private String children;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer stock;
}
