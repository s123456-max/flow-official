package com.alexmisko.service.impl;

import com.alexmisko.dao.MediaMapper;
import com.alexmisko.pojo.Media;
import com.alexmisko.service.MediaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements MediaService{

    @Autowired
    MediaMapper mediaMapper;

    @Override
    public List<Media> getMediaList(Long videoId) {
        QueryWrapper<Media> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id", videoId).select(Media.class, info -> !info.getColumn().equals("digest"));
        List<Media> mediaList = mediaMapper.selectList(queryWrapper);
        return mediaList;
    }
    
}