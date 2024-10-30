package com.vs.article.model.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveDTO {

    private LocalDateTime time;

    private List<ArticleCardDTO> articles;
}
