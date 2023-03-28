package com.alexmisko.dao;

import org.apache.ibatis.annotations.Mapper;

import com.alexmisko.pojo.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface TagMapper extends BaseMapper<Tag>{
    
}
