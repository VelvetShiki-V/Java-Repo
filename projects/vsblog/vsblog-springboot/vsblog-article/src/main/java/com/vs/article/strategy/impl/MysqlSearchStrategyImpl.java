package com.vs.article.strategy.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.vs.article.entity.Article;
import com.vs.article.enums.ArticleEnums;
import com.vs.article.mapper.ArticleMapper;
import com.vs.article.model.dto.ArticleSearchDTO;
import com.vs.article.strategy.ArticleSearchStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.vs.article.constant.GlobalConstant.POST_TAG;
import static com.vs.article.constant.GlobalConstant.PRE_TAG;

@Service("mysqlSearchStrategyImpl")
@RequiredArgsConstructor
public class MysqlSearchStrategyImpl implements ArticleSearchStrategy {

    private final ArticleMapper articleMapper;

    @Override
    public List<ArticleSearchDTO> searchArticles(String keywords) {
        if (StringUtils.isBlank(keywords)) {
            return new ArrayList<>();
        }
        // 关键词模糊匹配
        List<Article> articles = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, ArticleEnums.Delete.IS_NOT_DELETED.getValue())
                .eq(Article::getStatus, ArticleEnums.Status.PUBLIC.getValue())
                .and(i -> i.like(Article::getArticleTitle, keywords)
                        .or()
                        .like(Article::getArticleContent, keywords)));
        return articles.stream().map(item -> {
                    boolean isLowerCase = true;
                    String articleContent = item.getArticleContent();
                    int contentIndex = item.getArticleContent().indexOf(keywords.toLowerCase());
                    if (contentIndex == -1) {
                        contentIndex = item.getArticleContent().indexOf(keywords.toUpperCase());
                        if (contentIndex != -1) {
                            isLowerCase = false;
                        }
                    }
                    if (contentIndex != -1) {
                        int preIndex = contentIndex > 15 ? contentIndex - 15 : 0;
                        String preText = item.getArticleContent().substring(preIndex, contentIndex);
                        int last = contentIndex + keywords.length();
                        int postLength = item.getArticleContent().length() - last;
                        int postIndex = postLength > 35 ? last + 35 : last + postLength;
                        String postText = item.getArticleContent().substring(contentIndex, postIndex);
                        if (isLowerCase) {
                            articleContent = (preText + postText).replaceAll(keywords.toLowerCase(), PRE_TAG + keywords.toLowerCase() + POST_TAG);
                        } else {
                            articleContent = (preText + postText).replaceAll(keywords.toUpperCase(), PRE_TAG + keywords.toUpperCase() + POST_TAG);
                        }
                    } else {
                        return null;
                    }
                    isLowerCase = true;
                    int titleIndex = item.getArticleTitle().indexOf(keywords.toLowerCase());
                    if (titleIndex == -1) {
                        titleIndex = item.getArticleTitle().indexOf(keywords.toUpperCase());
                        if (titleIndex != -1) {
                            isLowerCase = false;
                        }
                    }
                    String articleTitle;
                    if (isLowerCase) {
                        articleTitle = item.getArticleTitle().replaceAll(keywords.toLowerCase(), PRE_TAG + keywords.toLowerCase() + POST_TAG);
                    } else {
                        articleTitle = item.getArticleTitle().replaceAll(keywords.toUpperCase(), PRE_TAG + keywords.toUpperCase() + POST_TAG);
                    }
                    return ArticleSearchDTO.builder()
                            .id(item.getId())
                            .articleTitle(articleTitle)
                            .articleContent(articleContent)
                            .build();
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
