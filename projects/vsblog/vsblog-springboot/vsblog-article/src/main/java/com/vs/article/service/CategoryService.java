package com.vs.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vs.article.entity.Category;
import com.vs.article.model.dto.CategoryAdminDTO;
import com.vs.article.model.dto.CategoryDTO;
import com.vs.article.model.dto.CategoryOptionDTO;
import com.vs.article.model.vo.ArticleFilterVO;
import com.vs.article.model.vo.CategoryVO;
import com.vs.framework.model.dto.PageResultDTO;

import java.util.List;

public interface CategoryService extends IService<Category> {

    List<CategoryDTO> listCategories();

    PageResultDTO<CategoryAdminDTO> listCategoriesAdmin(String keywords);

    List<CategoryOptionDTO> listCategoriesBySearch(String keywords);

    void deleteCategories(List<Integer> categoryIds);

    void saveOrUpdateCategory(CategoryVO categoryVO);
}

