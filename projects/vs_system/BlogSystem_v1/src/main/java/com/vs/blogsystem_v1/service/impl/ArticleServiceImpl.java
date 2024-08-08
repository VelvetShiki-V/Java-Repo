package com.vs.blogsystem_v1.service.impl;

import com.vs.pojo.Article;
import com.vs.blogsystem_v1.mapper.ArticleMapper;
import com.vs.blogsystem_v1.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author velvetshiki
 * @since 2024-07-22
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

}
