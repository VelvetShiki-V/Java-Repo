package com.vs.article.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vs.article.entity.Article;
import com.vs.article.entity.ArticleTag;
import com.vs.article.entity.Category;
import com.vs.article.entity.Tag;
import com.vs.article.exception.CustomException;
import com.vs.article.mapper.AdminArticleMapper;
import com.vs.article.mapper.TagMapper;
import com.vs.article.model.dto.ArticleAdminDTO;
import com.vs.article.model.dto.ArticleAdminViewDTO;
import com.vs.article.model.vo.ArticleFilterVO;
import com.vs.article.model.vo.ArticleVO;
import com.vs.article.service.AdminArticleService;
import com.vs.article.service.ArticleTagService;
import com.vs.article.service.CategoryService;
import com.vs.article.service.TagService;
import com.vs.framework.model.dto.PageResultDTO;
import com.vs.framework.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminArticleServiceImpl extends ServiceImpl<AdminArticleMapper, Article> implements AdminArticleService {

    private final AdminArticleMapper adminArticleMapper;

    private final CategoryService categoryService;

    private final ArticleTagService articleTagService;

    private final TagService tagService;

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
        // tagNames查询(先根据article tag获取tagId, 再从tag表获取tagName)
        List<Integer> tagIds = new ArrayList<>();
        articleTagService.lambdaQuery().eq(ArticleTag::getArticleId, articleId)
                .list().forEach(articleTag -> tagIds.add(articleTag.getTagId()));
        List<String> tagNames = new ArrayList<>();
        tagService.lambdaQuery().in(Tag::getId, tagIds)
                .list().forEach(tag -> tagNames.add(tag.getTagName()));
        // category查询
        Category category = categoryService.getById(article.getCategoryId());
        // 属性拷贝
        ArticleAdminViewDTO viewArticle = new ArticleAdminViewDTO();
        BeanUtil.copyProperties(article, viewArticle, CopyOptions.create().setIgnoreNullValue(true));
        viewArticle.setCategoryName(category.getCategoryName());
        viewArticle.setTagNames(tagNames);
        return viewArticle;
    }

    // 修改编辑文章
    @SneakyThrows
    @Override
    public void editArticle(ArticleVO articleVO) {
        // TODO: 自定义添加或删除标签（标签最大为3个），删除或更新分类（1个已存在的）
        //  置顶和推荐修改，文章内容修改（封面标题内容发布状态等）
        // 封装article对象
        Article article = lambdaQuery().eq(Article::getId, articleVO.getId()).one();
        if(Objects.isNull(article)) {
            throw new CustomException("404", "文章不存在", HttpStatus.NOT_FOUND);
        }
        // 分类修改
        article.setCategoryId(categoryService.lambdaQuery()
                .eq(Category::getCategoryName, articleVO.getCategoryName())
                .one()
                .getId());
        // 自定义tag新增或删除
        updateTag(articleVO, article.getId());
        // 存入新的article对象
        saveOrUpdate(article);
    }

    private void updateTag(ArticleVO articleVO, Integer id) {
        // 1. 把已存在的tag剔除，防止冗余插入
        // 2. 不存在的tag新增的tag表
        // 3. 文章与tag映射关系条目新增
    }
}
