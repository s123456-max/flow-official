package com.alexmisko.dao;

import org.apache.ibatis.annotations.Mapper;

import com.alexmisko.pojo.MsgContent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface MsgContentMapper extends BaseMapper<MsgContent>{
    
}
