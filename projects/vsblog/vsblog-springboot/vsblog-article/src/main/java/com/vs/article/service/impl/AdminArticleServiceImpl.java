package com.vs.article.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vs.article.constant.ArticleConstants;
import com.vs.article.entity.*;
import com.vs.article.enums.ArticleEnums;
import com.vs.article.enums.FileExtEnum;
import com.vs.article.enums.FilePathEnum;
import com.vs.article.exception.CustomException;
import com.vs.article.mapper.AdminArticleMapper;
import com.vs.article.mapper.ArticleTagMapper;
import com.vs.article.mapper.TagMapper;
import com.vs.article.model.dto.ArticleAdminDTO;
import com.vs.article.model.dto.ArticleAdminViewDTO;
import com.vs.article.model.vo.ArticleDeleteVO;
import com.vs.article.model.vo.ArticleFilterVO;
import com.vs.article.model.vo.ArticleTopFeaturedVO;
import com.vs.article.model.vo.ArticleVO;
import com.vs.article.service.AdminArticleService;
import com.vs.article.service.ArticleTagService;
import com.vs.article.service.CategoryService;
import com.vs.article.service.TagService;
import com.vs.article.strategy.context.FileUploadStrategyContext;
import com.vs.framework.model.dto.PageResultDTO;
import com.vs.framework.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminArticleServiceImpl extends ServiceImpl<AdminArticleMapper, Article> implements AdminArticleService {

    private final AdminArticleMapper adminArticleMapper;

    private final CategoryService categoryService;

    private final ArticleTagService articleTagService;

    private final TagService tagService;

    private final TagMapper tagMapper;

    private final ArticleTagMapper articleTagMapper;

    private final FileUploadStrategyContext fileUploadStrategyContext;

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
        if(CollectionUtils.isNotEmpty(tagIds)) {
            tagService.lambdaQuery().in(Tag::getId, tagIds)
                    .list().forEach(tag -> tagNames.add(tag.getTagName()));
        }
        // category查询
        Category category = categoryService.getById(article.getCategoryId());
        // 属性拷贝
        ArticleAdminViewDTO viewArticle = new ArticleAdminViewDTO();
        BeanUtil.copyProperties(article, viewArticle, CopyOptions.create().setIgnoreNullValue(true));
        viewArticle.setCategoryName(category.getCategoryName());
        viewArticle.setTagNames(tagNames);
        return viewArticle;
    }

    // 新增或更新文章
    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateArticle(ArticleVO articleVO) {
        // 封装article对象准备更新
        Article article = new Article();
        // 基本内容修改
        BeanUtil.copyProperties(articleVO, article, CopyOptions.create().setIgnoreNullValue(true));
        // TODO: 用户信息设置(根据已登录用户id存储)
        article.setUserId(1);
        // 分类修改（1个已存在的）
        article.setCategoryId(categoryService.lambdaQuery()
                .eq(Category::getCategoryName, articleVO.getCategoryName())
                .one()
                .getId());
        // 自定义tag新增或删除（每篇文章标签最大为3个）
        if(Objects.nonNull(article.getId())) updateTag(articleVO);
        // 如果文章不存在，则新增文章
        if(Objects.nonNull(article.getId())) article.setUpdateTime(LocalDateTime.now());
        else article.setCreateTime(LocalDateTime.now());
        saveOrUpdate(article);
        // 异步通知mq
        // TODO...
    }

    // 批量更改文章删除状态
    @Override
    public void deleteStateChange(ArticleDeleteVO articleDeleteVO) {
        // 如果文章id不存在则不做处理
        List<Article> articles = articleDeleteVO.getIds().stream().map(
                id -> Article.builder()
                        .id(id)
                        .isDelete(articleDeleteVO.getIsDelete())
                        .build()
        ).toList();
        updateBatchById(articles);
    }

    // 物理批量删除文章
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(List<Integer> articleIds) {
        // 先删除文章与标签映射
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>().in(ArticleTag::getArticleId, articleIds));
        // 再删除文章
        adminArticleMapper.deleteByIds(articleIds);
    }

    // 修改置顶或推荐文章状态
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTopFeaturedArticles(ArticleTopFeaturedVO articleTopFeaturedVO) {
        Article article = Article.builder()
                .id(articleTopFeaturedVO.getId())
                .isTop(articleTopFeaturedVO.getIsTop())
                .isFeatured(articleTopFeaturedVO.getIsFeatured())
                .build();
        adminArticleMapper.updateById(article);
    }

    // 导入文章至数据库和minio图床
    @SneakyThrows
    @Override
    public void importArticle(MultipartFile file) {
        // 解析文章标题
        String title = Objects.requireNonNull(file.getOriginalFilename()).substring(0, file.getOriginalFilename().lastIndexOf("."));
        // 解析文章内容
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            while (reader.ready()) {
                // ascii转字符
                content.append((char) reader.read());
            }
        } catch (IOException e) {
            log.error("导入文章失败, IO错误", e);
            throw new RuntimeException("导入文章失败, IO错误: " + e);
        }
        // 存储数据库
        saveOrUpdateArticle(ArticleVO.builder()
                .articleTitle(title)
                .categoryName("默认分类")
                .articleContent(content.toString())
                .status(ArticleConstants.Status.DRAFT)
                .build());
    }

    // 批量导出文章
    @SneakyThrows
    @Override
    public List<String> exportArticles(List<Integer> articleIds) {
        // 借助minio上传文章后通过返回urls列表提供下载
        List<Article> articles = lambdaQuery().in(Article::getId, articleIds).list();
        List<String> urls = new ArrayList<>();
        for(Article article: articles) {
            // 转字节流上传
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(article.getArticleContent().getBytes())) {
                String url = fileUploadStrategyContext.executeUploadStrategy(inputStream,
                        FilePathEnum.ARTICLE.getPath(), article.getArticleTitle() + FileExtEnum.MD.getExtName());
                urls.add(url);
            } catch (Exception e) {
                log.error("导出文章字节流上传minio失败", e);
                throw new CustomException("500", "导出文章字节流上传minio失败" + e, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return urls;
    }

    // 更新文章与标签
    @Transactional(rollbackFor = Exception.class)
    public void updateTag(ArticleVO articleVO) {
        // 判断标签是否需要更新（添加或删除）
        if(Objects.equals(articleVO.getTagNames(), tagMapper.listTagNameByArticleId(articleVO.getId()))) {
            log.debug("标签无需更新");
            return;
        }
        log.debug("标签需要更新");
        // 先统一将article-tag映射关系清理
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, articleVO.getId()));
        // 1. 把已存在的tag剔除，防止冗余插入
        List<String> newTagNames = new ArrayList<>();
        for(String currentTag: articleVO.getTagNames()) {
            if(Objects.isNull(tagService.lambdaQuery().eq(Tag::getTagName, currentTag).one())) {
                newTagNames.add(currentTag);
            }
        }
        // 2. 把不存在的tag新增到tag表
        if(CollectionUtils.isNotEmpty(newTagNames)) {
            List<Tag> newTags = newTagNames.stream().map(tag -> Tag.builder()
                    .tagName(tag)
                    .createTime(DateTime.now())
                    .build())
                    .collect(Collectors.toList());
            tagService.saveBatch(newTags);
        }
        // 3. 文章与tag映射关系条目更新
        // 3.1 获取新增tag和已存在tag的Id
        List<Integer> newTagIds = new ArrayList<>();
        for(String name: articleVO.getTagNames()) {
            newTagIds.add(tagService.lambdaQuery().eq(Tag::getTagName, name).one().getId());
        }
        List<ArticleTag> records = new ArrayList<>();
        for(Integer id: newTagIds) {
            records.add(ArticleTag.builder().articleId(articleVO.getId()).tagId(id).build());
        }
        // 3.2 映射关系存储
        articleTagService.saveBatch(records);
    }
}
