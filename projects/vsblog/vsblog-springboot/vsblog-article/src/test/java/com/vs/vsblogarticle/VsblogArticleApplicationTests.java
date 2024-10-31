package com.vs.vsblogarticle;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.vs.article.ArticleApplication;
import com.vs.article.model.dto.ArchiveDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {ArticleApplication.class})
class VsblogArticleApplicationTests {

    @Test
    void test1() {
//        LocalDateTime start = LocalDateTimeUtil.parse("2020-02", DatePattern.NORM_MONTH_FORMATTER);
//        LocalDateTime end = LocalDateTimeUtil.parse("2020-03", DatePattern.NORM_MONTH_FORMATTER);
//        Duration between = LocalDateTimeUtil.between(start, end);
//        System.out.println(between.toDays());

        List<String> list = new ArrayList<>(List.of("2024-10","2024-10", "2024-06", "2024-01","2024-10", "2024-12", "2023-12","2024-10", "2020-01"));
        list.sort((pre, next) -> {
            LocalDateTime pred = LocalDateTimeUtil.parse(pre, DatePattern.NORM_MONTH_PATTERN);
            LocalDateTime nexd = LocalDateTimeUtil.parse(next, DatePattern.NORM_MONTH_PATTERN);
            return Math.toIntExact(LocalDateTimeUtil.between(pred, nexd).toDays());
        });
        System.out.println(list);
    }

}
