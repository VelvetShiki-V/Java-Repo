package com.vs.article.controller;

import com.vs.article.model.dto.*;
import com.vs.article.model.vo.ArticleFilterVO;
import com.vs.article.model.vo.ArticlePasswordVO;
import com.vs.article.service.ArticleService;
import com.vs.article.strategy.context.ArticleSearchStrategyContext;
import com.vs.framework.model.dto.PageResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.vs.framework.model.vo.ResultVO;
import java.util.List;

@Tag(name = "文章API")
@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    private final ArticleSearchStrategyContext searchStrategyContext;

    @Operation(summary = "获取id文章")
    @GetMapping("/{articleId}")
    public ResultVO<ArticleDTO> getArticle(@PathVariable("articleId") Integer articleId) {
        return ResultVO.ok(articleService.getArticle(articleId));
    }

    @Operation(summary = "获取所有文章")
    @GetMapping("/all")
    public ResultVO<PageResultDTO<ArticleCardDTO>> listArticles() {
        return ResultVO.ok(articleService.listArticles());
    }

    @Operation(summary = "获取置顶和推荐文章")
    @GetMapping("/topFeatured")
    public ResultVO<ArticleTopFeaturedDTO> listTopFeaturedArticles() {
        return ResultVO.ok(articleService.listTopFeaturedArticles());
    }

    @Operation(summary = "获取分类Id文章")
    @GetMapping("/category")
    public ResultVO<PageResultDTO<ArticleCardDTO>> listCategoryArticles(@RequestParam("id") Integer categoryId) {
        return ResultVO.ok(articleService.listCategoryArticles(categoryId));
    }

    @Operation(summary = "获取标签Id文章")
    @GetMapping("/tag")
    public ResultVO<PageResultDTO<ArticleCardDTO>> listTagArticles(@RequestParam("id") Integer tagId) {
        return ResultVO.ok(articleService.listTagArticles(tagId));
    }

    @Operation(summary = "权限文章校验密码")
    @PostMapping("/access")
    public ResultVO<?> accessPrivateArticle(@Valid @RequestBody ArticlePasswordVO articlePasswordVO) {
        articleService.accessPrivateArticle(articlePasswordVO);
        return ResultVO.ok();
    }

    @Operation(summary = "获取所有文章归档")
    @GetMapping("/archives")
    public ResultVO<PageResultDTO<ArchiveDTO>> listArchives() {
        return ResultVO.ok(articleService.listArchives());
    }

    @Operation(summary = "搜索文章")
    @PostMapping("/search")
    public ResultVO<List<ArticleSearchDTO>> listSearchedArticles(@RequestParam("keywords") String keywords) {
        return ResultVO.ok(searchStrategyContext.executeSearchStrategy(keywords));
    }

}

