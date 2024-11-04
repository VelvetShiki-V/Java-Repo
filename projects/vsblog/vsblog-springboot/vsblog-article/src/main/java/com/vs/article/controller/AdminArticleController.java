package com.vs.article.controller;

import com.vs.article.model.dto.ArticleAdminDTO;
import com.vs.article.model.dto.ArticleAdminViewDTO;
import com.vs.article.model.vo.*;
import com.vs.article.service.AdminArticleService;
import com.vs.framework.model.dto.PageResultDTO;
import com.vs.framework.model.vo.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin")
@Tag(name = "文章后台管理API")
@RequiredArgsConstructor
public class AdminArticleController {

    private final AdminArticleService adminArticleService;


    @Operation(summary = "获取后台过滤条件的文章")
    @GetMapping("/articles")
    @Parameters({
            @Parameter(name = "current", description = "页码"),
            @Parameter(name = "size", description = "条数"),
            @Parameter(name = "keywords", description = "文章关键字"),
            @Parameter(name = "categoryId", description = "分类id"),
            @Parameter(name = "tagId", description = "标签id"),
            @Parameter(name = "albumId", description = "相簿id"),
            @Parameter(name = "loginType", description = "登录类型"),
            @Parameter(name = "type", description = "文章类型"),
            @Parameter(name = "status", description = "文章状态"),
            @Parameter(name = "startTime", description = "发布时间"),
            @Parameter(name = "endTime", description = "截止时间"),
            @Parameter(name = "isDelete", description = "是否删除", required = true),
            @Parameter(name = "isReview", description = "是否审核"),
            @Parameter(name = "isTop", description = "是否置顶"),
            @Parameter(name = "isFeatured", description = "是否推荐"),
    })
    public ResultVO<PageResultDTO<ArticleAdminDTO>> listAdminArticles(ArticleFilterVO articleFilterVO) {
        return ResultVO.ok(adminArticleService.listAdminArticles(articleFilterVO));
    }

    @Operation(summary = "根据id获取文章")
    @GetMapping("/articles/{articleId}")
    public ResultVO<ArticleAdminViewDTO> getAdminArticle(@PathVariable("articleId") Integer articleId) {
        return ResultVO.ok(adminArticleService.getAdminArticle(articleId));
    }

    @Operation(summary = "修改编辑文章")
    @PostMapping("/articles")
    public ResultVO<?> editArticle(@Valid @RequestBody ArticleVO articleVO) {
        adminArticleService.editArticle(articleVO);
        return ResultVO.ok();
    }

    @Operation(summary = "逻辑删除或恢复文章")
    @PutMapping("/articles")
    public ResultVO<?> deleteStateChange(@Valid @RequestBody ArticleDeleteVO articleDeleteVO) {
        adminArticleService.deleteStateChange(articleDeleteVO);
        return ResultVO.ok();
    }

    @Operation(summary = "物理删除文章")
    @DeleteMapping("/articles/delete")
    public ResultVO<?> deleteArticle(@RequestBody List<Integer> articleIds) {
        adminArticleService.deleteArticle(articleIds);
        return ResultVO.ok();
    }

    @Operation(summary = "修改置顶和推荐文章")
    @PutMapping("/articles/topFeatured")
    public ResultVO<?> updateTopFeaturedArticles(@Valid @RequestBody ArticleTopFeaturedVO articleTopFeaturedVO) {
        adminArticleService.updateTopFeaturedArticles(articleTopFeaturedVO);
        return ResultVO.ok();
    }

    // 文章编辑相关
    @Operation(summary = "文章上传图片，返回图片url")
    @PostMapping("/articles/images")
    public ResultVO<String> uploadArticleImages(@RequestParam MultipartFile file) {
        // TODO: uploadStrategy
        return ResultVO.ok();
    }

    @Operation(summary = "批量导入文章")
    @PostMapping("/articles/import")
    public ResultVO<?> importArticle(@RequestParam String type, MultipartFile file) {
        // TODO: 接收文章存入对象存储strategy
        return ResultVO.ok();
    }

    @Operation(summary = "批量导出文章")
    @PostMapping("/articles/export")
    public ResultVO<List<String>> exportArticle(@RequestBody List<Integer> articleIds) {
        // TODO: es strategy
        return ResultVO.ok();
    }
}
