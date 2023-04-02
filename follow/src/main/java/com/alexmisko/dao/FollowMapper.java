package com.alexmisko.dao;

import org.apache.ibatis.annotations.Mapper;

import com.alexmisko.pojo.Follow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface FollowMapper extends BaseMapper<Follow>{

}