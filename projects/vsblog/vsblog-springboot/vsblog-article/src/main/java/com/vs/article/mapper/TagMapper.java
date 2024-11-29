package com.vs.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vs.article.entity.Tag;
import com.vs.article.model.dto.TagAdminDTO;
import com.vs.article.model.dto.TagDTO;
import com.vs.article.model.vo.ArticleFilterVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    // 获取tag列表（含每个标签被文章引用统计）
    List<TagDTO> listTags();

    // 获取引用最多的前十个tag列表
    List<TagDTO> listTopTenTags();

    // 根据文章id查询所有tag名称
    List<String> listTagNameByArticleId(@Param("articleId") Integer articleId);

    // 分页查询tag列表（含文章引用次数）
    List<TagAdminDTO> listTagsAdmin(@Param("current") Long current,
                                    @Param("size") Long size,
                                    @Param("keywords")String keywords);
}

