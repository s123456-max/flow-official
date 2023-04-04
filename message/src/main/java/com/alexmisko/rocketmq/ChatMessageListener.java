package com.alexmisko.rocketmq;


import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alexmisko.pojo.MsgContent;
import com.alexmisko.service.MsgContentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RocketMQMessageListener(consumerGroup = "chat-consumer", topic = "message_chat")
public class ChatMessageListener implements RocketMQListener<MsgContent>{

    @Autowired
    MsgContentService msgContentService;

    @Override
    public void onMessage(MsgContent msgContent){
        log.info("message: [{}]", msgContent);
        msgContentService.storeChatMessage(msgContent);
    }
}
