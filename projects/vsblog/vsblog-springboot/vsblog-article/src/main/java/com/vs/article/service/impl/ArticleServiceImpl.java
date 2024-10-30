package com.vs.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vs.article.constant.ArticleConstants;
import com.vs.article.entity.Article;
import com.vs.article.enums.ArticleEnums;
import com.vs.article.mapper.ArticleMapper;
import com.vs.article.model.dto.ArticleCardDTO;
import com.vs.article.model.dto.ArticleDTO;
import com.vs.article.model.dto.ArticleTopFeaturedDTO;
import com.vs.article.service.ArticleService;
import com.vs.framework.model.dto.PageResultDTO;
import com.vs.framework.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
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
                .eq(Article::getIsDelete, ArticleEnums.Delete.IS_NOT_DELETED)
                .in(Article::getStatus, ArticleConstants.Status.PUBLIC, ArticleConstants.Status.PRIVATE);
        CompletableFuture<Long> asyncCount = CompletableFuture.supplyAsync(() -> articleMapper.selectCount(queryWrapper));
        // 查询文章
        List<ArticleCardDTO> articles = articleMapper.listArticleCards(PageUtil.getPageOffset(), PageUtil.getPageSize());
        return new PageResultDTO<>(articles, asyncCount.get());
    }

    // 获取置顶推荐文章
    @SneakyThrows
    @Override
    public ArticleTopFeaturedDTO listTopFeaturedArticles() {
        // 获取置顶和推荐文章列表集
        List<ArticleCardDTO> articles = articleMapper.listTopFeaturedArticleCards();
        if(articles.isEmpty()) return new ArticleTopFeaturedDTO();
        else if(articles.size() > 3) {
            // 大于3篇则仅显示3篇
            articles = articles.subList(0, 3);
        }
        // 置顶与推荐拆分
        ArticleTopFeaturedDTO topArticleDTO = new ArticleTopFeaturedDTO();
        topArticleDTO.setTopArticle(articles.get(0));
        articles.remove(0);
        topArticleDTO.setFeaturedArticles(articles);
        return topArticleDTO;
    }

    // 分类获取文章
    @SneakyThrows
    @Override
    public PageResultDTO<ArticleCardDTO> listCategoryArticles(Integer categoryId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<Article>()
                .eq(Article::getCategoryId, categoryId);
        CompletableFuture<Long> asyncCount = CompletableFuture
                .supplyAsync(() -> articleMapper.selectCount(queryWrapper));
        List<ArticleCardDTO> articles = articleMapper.listCategoryArticleCards(
                PageUtil.getPageOffset(), PageUtil.getPageSize(), categoryId);
        return new PageResultDTO<>(articles, asyncCount.get());
    }

    // 根据id获取文章（仅展示公开和私密文章）
    @SneakyThrows
    @Override
    public ArticleDTO getArticle(Integer articleId) {
        // 先查询文章是否为草稿或私密文章
        Article articleForCheck = lambdaQuery().eq(Article::getId, articleId).one();
        if(Objects.isNull(articleForCheck) || articleForCheck.getStatus()
                .equals(ArticleConstants.Status.DRAFT)) return null;
        if(articleForCheck.getStatus().equals(ArticleConstants.Status.PRIVATE)) {
            // 先redis校验登录身份，再另发请求校验密码
            // TODO: redis登录身份校验
        }
        // 封装文章展示对象(文章本体和前后篇文章卡片)
        CompletableFuture<ArticleDTO> asyncArticle = CompletableFuture.supplyAsync(
                () -> articleMapper.getArticle(articleId));
        CompletableFuture<ArticleCardDTO> asyncPreArticleCard = CompletableFuture.supplyAsync(() -> {
            // 如果前一篇文章不存在则显示最后一篇文章
            ArticleCardDTO preArticleCard = articleMapper.getPreArticleCard(articleId);
            if(Objects.isNull(preArticleCard)) preArticleCard = articleMapper.getLastArticleCard();
            return preArticleCard;
        });
        CompletableFuture<ArticleCardDTO> asyncNextArticleCard = CompletableFuture.supplyAsync(() -> {
            // 如果后一篇文章不存在则显示第一篇文章
            ArticleCardDTO nextArticleCard = articleMapper.getNextArticleCard(articleId);
            if(Objects.isNull(nextArticleCard)) nextArticleCard = articleMapper.getFirstArticleCard();
            return nextArticleCard;
        });
        // 异步获取文章查询结果
        ArticleDTO articleDTO = asyncArticle.get();
        if(Objects.isNull(articleDTO)) {
            log.error("文章不存在, id:{}", articleId);
            return null;
        }
        articleDTO.setPreArticleCard(asyncPreArticleCard.get());
        articleDTO.setNextArticleCard(asyncNextArticleCard.get());
        // TODO: redis自增文章浏览量
        return articleDTO;
    }
}
