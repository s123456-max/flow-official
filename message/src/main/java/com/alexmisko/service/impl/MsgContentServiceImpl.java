package com.alexmisko.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexmisko.dao.MsgContentMapper;
import com.alexmisko.pojo.MsgContent;
import com.alexmisko.service.MsgContentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class MsgContentServiceImpl extends ServiceImpl<MsgContentMapper, MsgContent> implements MsgContentService{

    @Autowired
    MsgContentMapper msgContentMapper;

    @Override
    public void storeChatMessage(MsgContent msgContent) {
        msgContentMapper.insert(msgContent);
    }
    
}
