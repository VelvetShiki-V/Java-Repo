package com.vs.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vs.article.entity.Tag;
import com.vs.article.model.dto.TagAdminDTO;
import com.vs.article.model.dto.TagDTO;
import com.vs.article.model.vo.ArticleFilterVO;
import com.vs.article.model.vo.TagVO;
import com.vs.framework.model.dto.PageResultDTO;

import java.util.List;

public interface TagService extends IService<Tag> {

    List<TagDTO> getAllTags();

    List<TagDTO> listTopTenTags();

    PageResultDTO<TagAdminDTO> listTagsAdmin(String keywords);

    List<TagAdminDTO> listTagsAdminBySearch(String keywords);

    void saveOrUpdateTag(TagVO tagVO);

    void deleteTag(List<Integer> tagIdList);
}

