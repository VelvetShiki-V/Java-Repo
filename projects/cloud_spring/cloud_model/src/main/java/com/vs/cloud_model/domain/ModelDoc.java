package com.vs.cloud_model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelDoc {
    private String mid;
    private String name;
    private String owner;
    private String labels;
    private String properties;
    private String children;
}
