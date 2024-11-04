package com.vs.article.model.dto;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagAdminDTO {

    private Integer id;

    private String tagName;

    private Integer articleCount;

    private LocalDateTime createTime;

}
