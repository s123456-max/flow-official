package com.alexmisko.service.impl;

import org.springframework.stereotype.Service;

import com.alexmisko.dao.FavorMapper;
import com.alexmisko.pojo.Favor;
import com.alexmisko.service.FavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class FavorServiceImpl extends ServiceImpl<FavorMapper, Favor> implements FavorService{
    
}
