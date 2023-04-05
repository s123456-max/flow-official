package com.alexmisko.rocketmq;


import java.io.IOException;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alexmisko.component.WebSocketServer;
import com.alexmisko.pojo.Message;
import com.alexmisko.service.MessageService;
import com.alexmisko.utils.JsonUtils;
import com.alexmisko.vo.FavorMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RocketMQMessageListener(consumerGroup = "consumer-group", topic = "message_favor")
public class FavorMessageListener implements RocketMQListener<FavorMessage>{

    @Autowired
    private MessageService messageService;

    @Override
    public void onMessage(FavorMessage favorMessage){
        log.info("message: [{}]", favorMessage);
        try {
            WebSocketServer.sendFavorMessage(favorMessage.getReceiverId(), JsonUtils.objectToJson(favorMessage.getMessage()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message message = new Message();
        message.setMessage(favorMessage.getMessage().getContent());
        message.setType(favorMessage.getMessage().getType());
        message.setVideoId(favorMessage.getVideoId());
        message.setSenderId(favorMessage.getSenderId());
        message.setReceiverId(favorMessage.getReceiverId());
        messageService.storeMessage(message);
    }
}
