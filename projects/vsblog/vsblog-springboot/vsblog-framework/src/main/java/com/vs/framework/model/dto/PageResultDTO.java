package com.vs.framework.model.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResultDTO<T> {
    private List<T> records;
    private Long count;
}
