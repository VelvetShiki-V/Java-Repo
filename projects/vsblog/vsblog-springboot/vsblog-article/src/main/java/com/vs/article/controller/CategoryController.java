package com.vs.article.controller;

import com.vs.article.model.dto.CategoryAdminDTO;
import com.vs.article.model.dto.CategoryDTO;
import com.vs.article.model.dto.CategoryOptionDTO;
import com.vs.article.model.vo.CategoryVO;
import com.vs.article.service.CategoryService;
import com.vs.framework.model.dto.PageResultDTO;
import com.vs.framework.model.dto.ResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "文章分类API")
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "获取所有分类")
    @GetMapping("/categories/all")
    public ResultDTO<List<CategoryDTO>> listCategories() {
        return ResultDTO.ok(categoryService.listCategories());
    }

    @Operation(summary = "查看后台分类列表")
    @GetMapping("/admin/categories")
    public ResultDTO<PageResultDTO<CategoryAdminDTO>> listCategoriesAdmin(
            @RequestParam(value = "keywords", required = false) String keywords,
            @RequestParam(value = "current") Long current,
            @RequestParam(value = "size") Long size
    ) {
        return ResultDTO.ok(categoryService.listCategoriesAdmin(keywords));
    }

    @Operation(summary = "搜索文章分类")
    @GetMapping("/admin/categories/search")
    public ResultDTO<List<CategoryOptionDTO>> listCategoriesAdminBySearch(@RequestParam(value = "keywords", required = false) String keywords) {
        return ResultDTO.ok(categoryService.listCategoriesBySearch(keywords));
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("/admin/categories")
    public ResultDTO<?> deleteCategories(@RequestBody List<Integer> categoryIds) {
        categoryService.deleteCategories(categoryIds);
        return ResultDTO.ok();
    }

    @Operation(summary = "添加或修改分类")
    @PostMapping("/admin/categories")
    @Parameters({
            @Parameter(name = "id", description = "分类id"),
            @Parameter(name = "categoryName", description = "分类名称", required = true)
    })
    public ResultDTO<?> saveOrUpdateCategory(@Valid @RequestBody CategoryVO categoryVO) {
        categoryService.saveOrUpdateCategory(categoryVO);
        return ResultDTO.ok();
    }
}
