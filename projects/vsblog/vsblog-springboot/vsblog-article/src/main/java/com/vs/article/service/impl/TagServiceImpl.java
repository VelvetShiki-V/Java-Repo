package com.vs.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vs.article.mapper.TagMapper;
import com.vs.article.entity.Tag;
import com.vs.article.service.TagService;
import org.springframework.stereotype.Service;

@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}

