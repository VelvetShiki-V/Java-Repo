package com.vs.article.model.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryAdminDTO {

    private Integer id;

    private String categoryName;

    private Integer articleCount;

    private LocalDateTime createTime;

}
