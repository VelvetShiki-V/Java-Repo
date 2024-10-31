package com.vs.article.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vs.article.entity.Article;
import com.vs.article.mapper.AdminArticleMapper;
import com.vs.article.mapper.ArticleMapper;
import com.vs.article.model.dto.ArticleAdminDTO;
import com.vs.article.model.dto.ArticleAdminViewDTO;
import com.vs.article.model.vo.ArticleFilterVO;
import com.vs.article.service.AdminArticleService;
import com.vs.framework.model.dto.PageResultDTO;
import com.vs.framework.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminArticleServiceImpl extends ServiceImpl<AdminArticleMapper, Article> implements AdminArticleService {

    private final AdminArticleMapper adminArticleMapper;

    // 返回后台条件过滤筛选文章
    @SneakyThrows
    @Override
    public PageResultDTO<ArticleAdminDTO> listAdminArticles(ArticleFilterVO articleFilterVO) {
        CompletableFuture<Integer> asyncCount = CompletableFuture
                .supplyAsync(() -> adminArticleMapper.countAdminArticles(articleFilterVO));
        List<ArticleAdminDTO> filteredArticles = adminArticleMapper
                .listAdminArticles(PageUtil.getPageOffset(), PageUtil.getPageSize(), articleFilterVO);
        // TODO: 从redis获取文章id对应浏览量并设置到filteredArticles中
        return new PageResultDTO<>(filteredArticles, (long) asyncCount.get());
    }

    // 根据id获取后台文章
    @SneakyThrows
    @Override
    public ArticleAdminViewDTO getAdminArticle(Integer articleId) {
        Article article = lambdaQuery().eq(Article::getId, articleId).one();
        // tag list查询
        // TODO
        // category查询
        // 属性拷贝
        ArticleAdminViewDTO view = new ArticleAdminViewDTO();
        BeanUtil.copyProperties(article, view, CopyOptions.create().setIgnoreNullValue(true));
        return null;
    }
}
