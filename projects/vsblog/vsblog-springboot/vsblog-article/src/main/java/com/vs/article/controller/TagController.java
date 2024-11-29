package com.vs.article.controller;

import com.vs.article.model.dto.TagAdminDTO;
import com.vs.article.model.dto.TagDTO;
import com.vs.article.model.vo.TagVO;
import com.vs.article.service.TagService;
import com.vs.framework.model.dto.PageResultDTO;
import com.vs.framework.model.dto.ResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "文章标签API")
@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(summary = "获取所有标签")
    @GetMapping("/tags/all")
    public ResultDTO<List<TagDTO>> getAllTags() {
        return ResultDTO.ok(tagService.getAllTags());
    }

    @Operation(summary = "获取前十个标签")
    @GetMapping("/tags/topTen")
    public ResultDTO<List<TagDTO>> getTopTenTags() {
        return ResultDTO.ok(tagService.listTopTenTags());
    }

    @Operation(summary = "查询后台标签列表")
    @GetMapping("/admin/tags")
    public ResultDTO<PageResultDTO<TagAdminDTO>> listTagsAdmin(
            @RequestParam(value = "keywords", required = false) String keywords,
            @RequestParam(value = "current") Long current,
            @RequestParam(value = "size") Long size
    ) {
        return ResultDTO.ok(tagService.listTagsAdmin(keywords));
    }

    @Operation(summary = "搜索文章标签")
    @GetMapping("/admin/tags/search")
    public ResultDTO<List<TagAdminDTO>> listTagsAdminBySearch(@RequestParam(value = "keywords", required = false) String keywords) {
        return ResultDTO.ok(tagService.listTagsAdminBySearch(keywords));
    }

    @Operation(summary = "添加或修改标签")
    @PostMapping("/admin/tags")
    @Parameters({
            @Parameter(name = "id", description = "标签id"),
            @Parameter(name = "tagName", description = "标签名称", required = true)
    })
    public ResultDTO<?> saveOrUpdateTag(@Valid @RequestBody TagVO tagVO) {
        tagService.saveOrUpdateTag(tagVO);
        return ResultDTO.ok();
    }

    @Operation(summary = "删除标签")
    @DeleteMapping("/admin/tags")
    public ResultDTO<?> deleteTag(@RequestBody List<Integer> tagIdList) {
        tagService.deleteTag(tagIdList);
        return ResultDTO.ok();
    }
}
