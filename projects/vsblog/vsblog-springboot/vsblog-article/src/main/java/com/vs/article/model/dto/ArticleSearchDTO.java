package com.vs.article.model.dto;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleSearchDTO {

    private Integer id;

    private String articleTitle;

    private String articleContent;

    private Integer isDelete;

    private Integer status;
}
