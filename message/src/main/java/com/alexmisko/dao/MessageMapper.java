package com.alexmisko.dao;

import org.apache.ibatis.annotations.Mapper;

import com.alexmisko.pojo.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface MessageMapper extends BaseMapper<Message>{
    
}