package com.vs.article.controller;

import com.vs.article.enums.FilePathEnum;
import com.vs.article.model.dto.ArticleAdminDTO;
import com.vs.article.model.dto.ArticleAdminViewDTO;
import com.vs.article.model.vo.*;
import com.vs.article.service.AdminArticleService;
import com.vs.article.strategy.context.FileUploadStrategyContext;
import com.vs.framework.model.dto.PageResultDTO;
import com.vs.framework.model.dto.ResultDTO;
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
@RequestMapping("/adminArticles")
@Tag(name = "文章后台管理API")
@RequiredArgsConstructor
public class AdminArticleController {

    private final AdminArticleService adminArticleService;

    private final FileUploadStrategyContext fileUploadStrategyContext;


    @Operation(summary = "获取后台过滤条件的文章")
    @GetMapping
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
    public ResultDTO<PageResultDTO<ArticleAdminDTO>> listAdminArticles(ArticleFilterVO articleFilterVO) {
        return ResultDTO.ok(adminArticleService.listAdminArticles(articleFilterVO));
    }

    @Operation(summary = "根据id获取文章")
    @GetMapping("/{articleId}")
    public ResultDTO<ArticleAdminViewDTO> getAdminArticle(@PathVariable("articleId") Integer articleId) {
        return ResultDTO.ok(adminArticleService.getAdminArticle(articleId));
    }

    @Operation(summary = "修改编辑文章")
    @PostMapping
    public ResultDTO<?> saveOrUpdateArticle(@Valid @RequestBody ArticleVO articleVO) {
        adminArticleService.saveOrUpdateArticle(articleVO);
        return ResultDTO.ok();
    }

    @Operation(summary = "逻辑删除或恢复文章")
    @PutMapping
    public ResultDTO<?> deleteStateChange(@Valid @RequestBody ArticleDeleteVO articleDeleteVO) {
        adminArticleService.deleteStateChange(articleDeleteVO);
        return ResultDTO.ok();
    }

    @Operation(summary = "物理删除文章")
    @DeleteMapping
    public ResultDTO<?> deleteArticle(@RequestBody List<Integer> articleIds) {
        adminArticleService.deleteArticle(articleIds);
        return ResultDTO.ok();
    }

    @Operation(summary = "修改置顶和推荐文章")
    @PutMapping("/topFeatured")
    public ResultDTO<?> updateTopFeaturedArticles(@Valid @RequestBody ArticleTopFeaturedVO articleTopFeaturedVO) {
        adminArticleService.updateTopFeaturedArticles(articleTopFeaturedVO);
        return ResultDTO.ok();
    }

    // 文章编辑相关
    @Operation(summary = "文章上传图片，返回图片url，相同图片不会重复上传")
    @PostMapping("/images")
    public ResultDTO<String> uploadArticleImages(@RequestParam("file") MultipartFile file, @RequestParam("dirPath") String dirPath) {
        return ResultDTO.ok(fileUploadStrategyContext
                .executeUploadStrategy(file, FilePathEnum
                        .ARTICLE_IMAGE.getPath() + dirPath + FilePathEnum.DIR_SUFFIX.getPath()));
    }

    @Operation(summary = "导入文章")
    @PostMapping("/import")
    public ResultDTO<?> importArticle(@RequestParam("file") MultipartFile file) {
        // 前端图片上传完毕后，将替换好url的图片连同整个文章传入存储
        adminArticleService.importArticle(file);
        return ResultDTO.ok();
    }

    @Operation(summary = "批量导出文章")
    @PostMapping("/export")
    public ResultDTO<List<String>> exportArticles(@RequestBody List<Integer> articleIds) {
        return ResultDTO.ok(adminArticleService.exportArticles(articleIds));
    }
}
