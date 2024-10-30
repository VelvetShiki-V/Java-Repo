package com.vs.article.model.dto;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {

    private Integer id;

    private String tagName;

    private Integer count;

}
