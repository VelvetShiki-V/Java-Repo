package com.vs.article.controller;

import com.vs.article.model.dto.ArticleAdminDTO;
import com.vs.article.model.vo.*;
import com.vs.framework.model.dto.PageResultDTO;
import com.vs.framework.model.vo.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "文章后台管理API")
public class AdminArticleController {

    @Operation(summary = "获取后台所有文章，接收条件筛选可选请求参数")
    @GetMapping("/articles")
    public ResultVO<PageResultDTO<ArticleAdminDTO>> listAdminArticles(ArticleFilterVO articleFilterVO) {
        // TODO
        return ResultVO.ok();
    }

    @Operation(summary = "根据id获取文章")
    @GetMapping("/articles/{articleId}")
    public ResultVO<ArticleAdminViewVO> getAdminArticle(@PathVariable("articleId") Integer articleId) {
        // TODO
        return  ResultVO.ok();
    }

    @Operation(summary = "修改编辑文章")
    @PostMapping("/articles")
    public ResultVO<?> editArticle(@RequestBody ArticleVO articleVO) {
        // TODO
        return ResultVO.ok();
    }

    @Operation(summary = "逻辑删除或恢复文章")
    @PutMapping("/articles")
    public ResultVO<?> logicallyDeleteArticle(@Valid @RequestBody ArticleDeleteVO articleDeleteVO) {
        // TODO
        return ResultVO.ok();
    }

    @Operation(summary = "物理删除文章")
    @DeleteMapping("/articles/delete")
    public ResultVO<?> deleteArticle(@RequestBody List<Integer> articleIds) {
        // TODO
        return ResultVO.ok();
    }

    @Operation(summary = "修改置顶和推荐文章")
    @PutMapping("/articles/topFeatured")
    public ResultVO<?> updateTopFeaturedArticles(@Valid @RequestBody ArticleTopFeaturedVO articleTopFeaturedVO) {
        // TODO
        return ResultVO.ok();
    }

    // 文章编辑相关
    @Operation(summary = "文章上传图片，返回图片url")
    @PostMapping("/articles/images")
    public ResultVO<String> uploadArticleImages(@RequestParam MultipartFile file) {
        // TODO
        return ResultVO.ok();
    }

    @Operation(summary = "批量导入文章")
    @PostMapping("/articles/import")
    public ResultVO<?> importArticle(@RequestParam String type, MultipartFile file) {
        // TODO
        return ResultVO.ok();
    }

    @Operation(summary = "批量导出文章")
    @PostMapping("/articles/export")
    public ResultVO<List<String>> exportArticle(@RequestBody List<Integer> articleIds) {
        // TODO
        return ResultVO.ok();
    }
}
