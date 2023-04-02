package com.alexmisko.service.impl;

import org.springframework.stereotype.Service;

import com.alexmisko.dao.FollowMapper;
import com.alexmisko.pojo.Follow;
import com.alexmisko.service.FollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {
}