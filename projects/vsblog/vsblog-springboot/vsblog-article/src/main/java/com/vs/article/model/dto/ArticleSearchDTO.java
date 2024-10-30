package com.vs.article.model.dto;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleSearchDTO {

    // elastic search改造
    private Integer id;

    private String articleTitle;

    private String articleContent;

    private Integer isDeleted;

    private Integer status;
}
