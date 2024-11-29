package com.vs.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vs.article.entity.Category;
import com.vs.article.model.dto.CategoryAdminDTO;
import com.vs.article.model.dto.CategoryDTO;
import com.vs.article.model.vo.ArticleFilterVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    List<CategoryDTO> listCategories();

    List<CategoryAdminDTO> listCategoriesAdmin(@Param("current") Long current,
                                               @Param("size") Long size,
                                               @Param("keywords") String keywords);

}
