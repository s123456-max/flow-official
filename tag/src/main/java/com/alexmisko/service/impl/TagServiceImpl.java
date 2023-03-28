package com.alexmisko.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexmisko.dao.TagMapper;
import com.alexmisko.pojo.Tag;
import com.alexmisko.service.TagService;
import com.alexmisko.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService{

    @Autowired
    TagMapper tagMapper;

    @Override
    public Result<List<Tag>> getTagList(Long videoId) {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id", videoId);
        return Result.success(tagMapper.selectList(queryWrapper));
    }
    
}
