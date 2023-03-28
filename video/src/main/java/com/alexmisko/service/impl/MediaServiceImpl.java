package com.alexmisko.service.impl;

import com.alexmisko.dao.MediaMapper;
import com.alexmisko.pojo.Media;
import com.alexmisko.service.MediaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements MediaService{
}