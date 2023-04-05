package com.alexmisko.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexmisko.dao.MessageMapper;
import com.alexmisko.pojo.Message;
import com.alexmisko.service.MessageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService{

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void storeMessage(Message message){
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver_id", message.getReceiverId())
        .eq("sender_id", message.getSenderId())
        .eq("video_id", message.getVideoId())
        .eq("type", message.getType());
        if(messageMapper.selectOne(queryWrapper) == null){
            messageMapper.insert(message);
        }
    }
}