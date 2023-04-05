package com.alexmisko.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexmisko.dao.MessageMapper;
import com.alexmisko.pojo.Message;
import com.alexmisko.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService{

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void storeMessage(Message message){
        messageMapper.insert(message);
    }
}