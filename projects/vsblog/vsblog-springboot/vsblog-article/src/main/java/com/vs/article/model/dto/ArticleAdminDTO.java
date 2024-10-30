package com.vs.article.model.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleAdminDTO {

    private Integer id;

    private String articleCover;

    private String articleTitle;

    private LocalDateTime createTime;

    private Integer viewsCount;

    private String categoryName;

    private List<TagDTO> tagDTOs;

    private Integer isTop;

    private Integer isFeatured;

    private Integer isDelete;

    private Integer status;

    private Integer type;
}
