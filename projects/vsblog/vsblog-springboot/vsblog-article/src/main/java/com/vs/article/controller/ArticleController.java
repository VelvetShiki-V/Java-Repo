package com.vs.article.controller;

import com.vs.article.model.dto.ArticleCardDTO;
import com.vs.article.model.dto.ArticleDTO;
import com.vs.article.model.dto.ArticleSearchDTO;
import com.vs.article.model.dto.ArticleTopFeaturedDTO;
import com.vs.article.model.vo.ArticleFilterVO;
import com.vs.article.model.vo.ArticlePasswordVO;
import com.vs.article.service.ArticleService;
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
        // TODO
        return ResultVO.ok();
    }

    @Operation(summary = "权限文章校验密码")
    @PostMapping("/access")
    public ResultVO accessArticle(@Valid @RequestBody ArticlePasswordVO articlePasswordVO) {
        // TODO
        return ResultVO.ok();
    }

    @Operation(summary = "获取所有文章归档")
    @GetMapping("/archives")
    public ResultVO listArchives() {
        // TODO
        return ResultVO.ok();
    }

    @Operation(summary = "搜索文章")
    @PostMapping("/search")
    public ResultVO<List<ArticleSearchDTO>> listSearchedArticles(ArticleFilterVO articleFilterVO) {
        // TODO
        return ResultVO.ok();
    }

}

