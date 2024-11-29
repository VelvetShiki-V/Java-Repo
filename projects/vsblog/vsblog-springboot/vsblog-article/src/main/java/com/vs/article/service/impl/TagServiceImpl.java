package com.vs.article.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vs.article.entity.ArticleTag;
import com.vs.article.exception.CustomException;
import com.vs.article.mapper.ArticleTagMapper;
import com.vs.article.mapper.TagMapper;
import com.vs.article.entity.Tag;
import com.vs.article.model.dto.TagAdminDTO;
import com.vs.article.model.dto.TagDTO;
import com.vs.article.model.vo.TagVO;
import com.vs.article.service.TagService;
import com.vs.framework.model.dto.PageResultDTO;
import com.vs.framework.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service("tagService")
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private final TagMapper tagMapper;

    private final ArticleTagMapper articleTagMapper;

    @SneakyThrows
    @Override
    public List<TagDTO> getAllTags() {
        return tagMapper.listTags();
    }

    @SneakyThrows
    @Override
    public List<TagDTO> listTopTenTags() {
        return tagMapper.listTopTenTags();
    }

    @SneakyThrows
    @Override
    public PageResultDTO<TagAdminDTO> listTagsAdmin(String keywords) {
        List<TagAdminDTO> tags = tagMapper.listTagsAdmin(PageUtil.getPageOffset(), PageUtil.getPageSize(), keywords);
        return PageResultDTO.<TagAdminDTO>builder().count((long) tags.size()).records(tags).build();
    }

    @SneakyThrows
    @Override
    public List<TagAdminDTO> listTagsAdminBySearch(String keywords) {
        return tagMapper.listTagsAdmin(PageUtil.getPageOffset(), PageUtil.getPageSize(), keywords);
    }

    @SneakyThrows
    @Override
    public void saveOrUpdateTag(TagVO tagVO) {
        Tag existTag = lambdaQuery().eq(Tag::getTagName, tagVO.getTagName()).one();
        if(Objects.nonNull(existTag) && !existTag.getId().equals(tagVO.getId())) {
            log.warn("标签: {}已存在", existTag.getTagName());
            throw new CustomException("400", "标签已存在", HttpStatus.BAD_REQUEST);
        }
        Tag tag = BeanUtil.toBean(tagVO, Tag.class);
        tag.setUpdateTime(LocalDateTime.now());
        saveOrUpdate(tag);
    }

    @SneakyThrows
    @Override
    public void deleteTag(List<Integer> tagIdList) {
        if(articleTagMapper.selectCount(new LambdaQueryWrapper<ArticleTag>().in(ArticleTag::getTagId, tagIdList)) > 0) {
            log.error("标签删除失败，该标签仍存在文章内");
            throw new CustomException("400", "删除失败，该标签下存在文章", HttpStatus.BAD_REQUEST);
        }
        tagMapper.deleteByIds(tagIdList);
    }
}

