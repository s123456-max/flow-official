package com.alexmisko.rocketmq;


import java.io.IOException;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import com.alexmisko.component.WebSocketServer;
import com.alexmisko.utils.JsonUtils;
import com.alexmisko.vo.FavorMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RocketMQMessageListener(consumerGroup = "consumer-group", topic = "message_favor")
public class FavorMessageListener implements RocketMQListener<FavorMessage>{

    @Override
    public void onMessage(FavorMessage favorMessage){
        log.info("message: [{}]", favorMessage);
        try {
            WebSocketServer.sendFavorMessage(favorMessage.getReceiverId(), JsonUtils.objectToJson(favorMessage.getMessage()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
