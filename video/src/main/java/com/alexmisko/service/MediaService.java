package com.alexmisko.service;

import java.util.List;

import com.alexmisko.pojo.Media;
import com.baomidou.mybatisplus.extension.service.IService;

public interface MediaService extends IService<Media> {
    List<Media> getMediaList(Long videoId);
}