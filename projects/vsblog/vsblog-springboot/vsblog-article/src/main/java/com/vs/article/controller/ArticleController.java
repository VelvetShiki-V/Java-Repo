package com.vs.article.controller;

import com.vs.article.model.dto.ArticleCardDTO;
import com.vs.article.model.dto.TopFeaturedArticlesDTO;
import com.vs.article.service.ArticleService;
import com.vs.framework.model.dto.PageResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.vs.framework.model.vo.ResultVO;

@Tag(name = "文章API")
@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @Operation(summary = "获取所有文章")
    @GetMapping("/articles/all")
    public ResultVO<PageResultDTO<ArticleCardDTO>> listArticles() {
        return ResultVO.ok(articleService.listArticles());
    }

    @Operation(summary = "获取置顶和推荐文章")
    @GetMapping("/articles/topFeatured")
    public ResultVO<TopFeaturedArticlesDTO> listTopFeaturedArticles() {
        return ResultVO.ok(articleService.listTopFeaturedArticles());
    }
}

