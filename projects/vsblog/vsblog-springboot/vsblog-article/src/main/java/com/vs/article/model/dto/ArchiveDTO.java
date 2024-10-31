package com.vs.article.model.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveDTO {

    private String time;        // 将时间作为map的键

    private List<ArticleCardDTO> articles;  // 一键多值
}
