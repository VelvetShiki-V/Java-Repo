package com.vs.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.vs.article.entity.Article;
import com.vs.article.mapper.ArticleMapper;
import com.vs.article.model.dto.ArticleCardDTO;
import com.vs.article.model.dto.TopFeaturedArticlesDTO;
import com.vs.article.service.ArticleService;
import com.vs.framework.model.dto.PageResultDTO;
import com.vs.framework.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final ArticleMapper articleMapper;

    // 获取所有文章卡片
    @SneakyThrows       // 不显式捕获或声明异常的情况下，抛出受检异常（即 checked exceptions），从而简化异常处理代码
    @Override
    public PageResultDTO<ArticleCardDTO> listArticles() {
        // 查询条目数目
        LambdaQueryWrapper<Article> queryWrapper= new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, 0)
                .in(Article::getStatus, 1, 2);
        CompletableFuture<Long> asyncCount = CompletableFuture.supplyAsync(() -> articleMapper.selectCount(queryWrapper));
        // 查询文章
        List<ArticleCardDTO> articles = articleMapper.listArticles(PageUtil.getPageOffset(), PageUtil.getPageSize());
        return new PageResultDTO<>(articles, asyncCount.get());
    }

    // 获取置顶推荐文章
    @Override
    public TopFeaturedArticlesDTO listTopFeaturedArticles() {
        // 获取置顶和推荐文章列表集
        List<ArticleCardDTO> articles = articleMapper.listTopFeaturedArticles();
        if(articles.isEmpty()) return new TopFeaturedArticlesDTO();
        else if(articles.size() > 3) {
            // 大于3篇仅显示3篇
            articles = articles.subList(0, 3);
        }
        // 置顶与推荐拆分
        TopFeaturedArticlesDTO topArticleDTO = new TopFeaturedArticlesDTO();
        topArticleDTO.setTopArticle(articles.get(0));
        articles.remove(0);
        topArticleDTO.setFeaturedArticles(articles);
        return topArticleDTO;
    }
}
