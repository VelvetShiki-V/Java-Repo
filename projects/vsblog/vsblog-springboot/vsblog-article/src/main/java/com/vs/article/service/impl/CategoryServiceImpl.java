package com.vs.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vs.article.mapper.CategoryMapper;
import com.vs.article.entity.Category;
import com.vs.article.service.CategoryService;
import org.springframework.stereotype.Service;

/**
 * (Category)表服务实现类
 *
 * @author makejava
 * @since 2024-11-01 10:34:25
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}

