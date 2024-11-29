package com.vs.article.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vs.article.entity.Article;
import com.vs.article.exception.CustomException;
import com.vs.article.mapper.ArticleMapper;
import com.vs.article.mapper.CategoryMapper;
import com.vs.article.entity.Category;
import com.vs.article.model.dto.CategoryAdminDTO;
import com.vs.article.model.dto.CategoryDTO;
import com.vs.article.model.dto.CategoryOptionDTO;
import com.vs.article.model.vo.ArticleFilterVO;
import com.vs.article.model.vo.CategoryVO;
import com.vs.article.service.CategoryService;
import com.vs.framework.model.dto.PageResultDTO;
import com.vs.framework.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service("categoryService")
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final CategoryMapper categoryMapper;

    private final ArticleMapper articleMapper;

    @Override
    public List<CategoryDTO> listCategories() {
        return categoryMapper.listCategories();
    }

    @Override
    public PageResultDTO<CategoryAdminDTO> listCategoriesAdmin(String keywords) {
        List<CategoryAdminDTO> categories = categoryMapper.listCategoriesAdmin(
                PageUtil.getPageOffset(), PageUtil.getPageSize(), keywords
        );
        return PageResultDTO.<CategoryAdminDTO>builder()
                .records(categories)
                .count((long) categories.size())
                .build();
    }

    @Override
    public List<CategoryOptionDTO> listCategoriesBySearch(String keywords) {
        return categoryMapper.listCategoriesAdmin(
                    PageUtil.getPageOffset(), PageUtil.getPageSize(), keywords
        ).stream().map(tag -> BeanUtil.toBean(tag, CategoryOptionDTO.class))
        .collect(Collectors.toList());
    }

    @Override
    public void deleteCategories(List<Integer> categoryIds) {
        if(articleMapper.selectCount(new LambdaQueryWrapper<Article>().in(Article::getCategoryId, categoryIds)) > 0) {
            log.error("该分类下仍存在文章，删除失败");
            throw new CustomException("400", "该分类下仍存在文章，删除失败", HttpStatus.BAD_REQUEST);
        }
        categoryMapper.deleteByIds(categoryIds);
    }

    @Override
    public void saveOrUpdateCategory(CategoryVO categoryVO) {
        Category existCategory = lambdaQuery().eq(Category::getCategoryName, categoryVO.getCategoryName()).one();
        if(Objects.nonNull(existCategory) && !existCategory.getId().equals(categoryVO.getId())) {
            log.error("分类已存在，操作失败");
            throw new CustomException("400", "分类已存在，操作失败", HttpStatus.BAD_REQUEST);
        }
        Category category = BeanUtil.toBean(categoryVO, Category.class);
        category.setUpdateTime(LocalDateTime.now());
        saveOrUpdate(category);
    }
}

