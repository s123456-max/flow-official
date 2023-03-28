package com.alexmisko.service.impl;

import org.springframework.stereotype.Service;

import com.alexmisko.dao.TagMapper;
import com.alexmisko.pojo.Tag;
import com.alexmisko.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService{
    
}
