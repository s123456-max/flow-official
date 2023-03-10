package com.alexmisko.service.impl;

import com.alexmisko.dao.UserInfoMapper;
import com.alexmisko.pojo.UserInfo;
import com.alexmisko.service.UserInfoService;
import com.alexmisko.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public Result<UserInfo> getByUserId(Long id) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);
        return Result.success(userInfoMapper.selectOne(queryWrapper));
    }
}
